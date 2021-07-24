package com.toprest.controller

import android.util.Patterns
import com.toprest.core.extension.toCompletable
import com.toprest.model.AccountType
import com.toprest.model.SignUpScreen
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.translations.SignUpTranslations
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor
import java.lang.IllegalStateException
import java.util.*

private const val MIN_PASSWORD_LENGTH = 6

class SignUpControllerImpl(
    private val translations: SignUpTranslations,
    private val serialScheduler: Scheduler,
    private val routingActionsDispatcher: RoutingActionsDispatcher
) : SignUpController {

    private val screensToRoutingActions = mutableMapOf<SignUpScreen, (Router) -> Unit>(
        SignUpScreen.REGISTER_NAME to { router -> router.showRegisterName() },
        SignUpScreen.SELECT_ACCOUNT_TYPE to { router -> router.showAccountTypeSelection() },
        SignUpScreen.REGISTER_EMAIL to { router -> router.showRegisterEmail() },
        SignUpScreen.REGISTER_PASSWORD to { router -> router.showRegisterPassword() }
    )

    private val screenActions = mutableMapOf<SignUpScreen, Completable>(
        SignUpScreen.REGISTER_NAME to showScreen(SignUpScreen.SELECT_ACCOUNT_TYPE),
        SignUpScreen.SELECT_ACCOUNT_TYPE to showScreen(SignUpScreen.REGISTER_EMAIL),
        SignUpScreen.REGISTER_EMAIL to showScreen(SignUpScreen.REGISTER_PASSWORD),
        SignUpScreen.REGISTER_PASSWORD to setLoading(true)
    )

    private val screensProcessor = BehaviorProcessor.createDefault(Stack<SignUpScreen>())
    private val screens = screensProcessor
    private val screen = screens.filter { it.isNotEmpty() }.map { it.peek() }

    private val loading = BehaviorProcessor.createDefault(false)
    private val firstName = BehaviorProcessor.createDefault("")
    private val lastName = BehaviorProcessor.createDefault("")
    private val accountType = BehaviorProcessor.create<AccountType>()
    private val email = BehaviorProcessor.createDefault("")

    private val password = BehaviorProcessor.createDefault("")
    private val repeatedPassword = BehaviorProcessor.createDefault("")

    override fun setScreen(signUpScreen: SignUpScreen): Completable = screens
        .observeOn(serialScheduler)
        .firstOrError()
        .toCompletable {
            it.push(signUpScreen)
            screensProcessor.onNext(it)
        }

    override fun title(): Flowable<String> = screen
        .map {
            when (it) {
                SignUpScreen.REGISTER_NAME -> translations.nameTitle()
                SignUpScreen.SELECT_ACCOUNT_TYPE -> translations.accountTypeTitle()
                SignUpScreen.REGISTER_EMAIL -> translations.emailTitle()
                SignUpScreen.REGISTER_PASSWORD -> translations.passwordTitle()
                else -> throw IllegalStateException("Invalid screen enum")
            }
        }

    override fun actionButtonEnabled(): Flowable<Boolean> = screen.switchMap {
        when (it) {
            SignUpScreen.REGISTER_NAME -> actionButtonEnabledForNameScreen()
            SignUpScreen.SELECT_ACCOUNT_TYPE -> accountType.map { true }.startWithItem(false)
            SignUpScreen.REGISTER_EMAIL -> isEmailValid()
            SignUpScreen.REGISTER_PASSWORD -> actionButtonEnabledForPassword()
            else -> throw IllegalStateException("Invalid screen enum")
        }
    }

    override fun actionButtonText(): Flowable<String> = screen
        .map {
            when (it) {
                SignUpScreen.REGISTER_PASSWORD -> translations.createAccount()
                else -> translations.next()
            }
        }

    override fun actionButtonClicked(): Completable = screen
        .firstOrError()
        .flatMapCompletable { screenActions.getValue(it) }

    override fun previous(): Completable =
        Completable.fromAction { routingActionsDispatcher.dispatch(Router::goBack) }
            .andThen(
                screens
                    .observeOn(serialScheduler)
                    .firstOrError()
                    .toCompletable {
                        it.pop()
                        screensProcessor.onNext(it)
                    }
            )

    override fun loading(): Flowable<Boolean> = loading

    override fun firstName(): Flowable<String> = firstName

    override fun lastName(): Flowable<String> = lastName

    override fun accountType(): Flowable<AccountType> = accountType

    override fun email(): Flowable<String> = email

    override fun isEmailValid(): Flowable<Boolean> = email.map { email ->
        if (email.isBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    override fun password(): Flowable<String> = password

    override fun repeatedPassword(): Flowable<String> = repeatedPassword

    override fun isPasswordValid(): Flowable<Boolean> = password.map { it.length >= MIN_PASSWORD_LENGTH }

    override fun arePasswordsMatched(): Flowable<Boolean> = Flowable.combineLatest(
        password,
        repeatedPassword,
        { password, repeatedPassword -> password == repeatedPassword }
    )

    override fun setLoading(isLoading: Boolean): Completable = Completable.fromAction { loading.onNext(isLoading) }

    override fun setFirstName(firstName: String): Completable = Completable.fromAction { this.firstName.onNext(firstName) }

    override fun setLastName(lastName: String): Completable = Completable.fromAction { this.lastName.onNext(lastName) }

    override fun setAccountType(accountType: AccountType): Completable = Completable.fromAction { this.accountType.onNext(accountType) }

    override fun setEmail(email: String): Completable = Completable.fromAction { this.email.onNext(email) }

    override fun setPassword(password: String): Completable = Completable.fromAction { this.password.onNext(password) }

    override fun setRepeatedPassword(repeatedPassword: String): Completable =
        Completable.fromAction { this.repeatedPassword.onNext(repeatedPassword) }

    private fun actionButtonEnabledForNameScreen() = Flowable.combineLatest(
        firstName,
        lastName,
        { firstName, lastName -> firstName.isNotBlank() && lastName.isNotBlank() }
    )

    private fun actionButtonEnabledForPassword() = Flowable.combineLatest(
        isPasswordValid(),
        arePasswordsMatched(),
        { isPasswordValid, arePasswordsMatched -> isPasswordValid && arePasswordsMatched}
    )

    private fun showScreen(screen: SignUpScreen) =
        Completable.fromAction { routingActionsDispatcher.dispatch(screensToRoutingActions.getValue(screen)) }
}
