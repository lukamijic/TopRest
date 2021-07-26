package com.toprest.login.ui

import android.util.Patterns
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.usecase.Login
import io.reactivex.rxjava3.core.Completable.*
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor
import java.util.concurrent.TimeUnit

private const val MIN_PASSWORD_LENGTH = 6
private const val ERROR_DEBOUNCE = 1000L

private val LOGIN_ERROR_EVENT = Any()

class LoginViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val login: Login
) : BaseViewModel<LoginViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val email = BehaviorProcessor.create<String>()
    private val password = BehaviorProcessor.create<String>()
    private val loading = BehaviorProcessor.create<Boolean>()

    private val loginError = PublishProcessor.create<Any>()

    private val emailValid = email.map { email ->
        if (email.isBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private val passwordValid = password.map { it.length >= MIN_PASSWORD_LENGTH }

    init {
        query(
            combineLatest(
                emailValid,
                passwordValid
            ) { emailValid, passwordValid -> emailValid && passwordValid }
                .distinctUntilChanged()
                .map(LoginViewState::LoginButton)
        )

        query(
            merge(
                combineLatest(
                    email,
                    emailValid
                ) { email, isEmailValid -> (email.isEmpty() || isEmailValid).not() }
                    .debounce(ERROR_DEBOUNCE, TimeUnit.MILLISECONDS),
                email.map { false }
            )
                .distinctUntilChanged()
                .map(LoginViewState::EmailError)
        )

        query(
            merge(
                combineLatest(
                    password,
                    passwordValid
                ) { password, isPasswordValid -> (password.isEmpty() || isPasswordValid).not() }
                    .debounce(ERROR_DEBOUNCE, TimeUnit.MILLISECONDS),
                password.map { false }
            )
                .distinctUntilChanged()
                .map(LoginViewState::PasswordError)
        )

        query(
            merge(
                loginError.map { true },
                email.map { false },
                password.map { false }
            )
                .distinctUntilChanged()
                .map(LoginViewState::LoginError)
        )

        query(loading.observeOn(backgroundScheduler).map(LoginViewState::Loading))
    }

    fun setEmail(email: String) = runCommand(fromAction { this.email.onNext(email) })

    fun setPassword(password: String) = runCommand(fromAction { this.password.onNext(password) })

    fun login() {
        loading.onNext(true)
        runCommand(
            combineLatest(
                email,
                password,
                BiFunction(Login::Param)
            )
                .firstOrError()
                .flatMapCompletable { login(it) }
                .doOnComplete { dispatchRoutingAction(Router::showMainActivity) }
                .doOnError { loginError.onNext(LOGIN_ERROR_EVENT) }
                .doFinally { loading.onNext(false) }
        )
    }
}
