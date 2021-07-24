package com.toprest.ui.registerpassword

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseViewModel
import com.toprest.model.SignUpScreen
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.PublishProcessor
import java.util.concurrent.TimeUnit

private const val PASSWORD_ERROR_DEBOUNCE = 1000L
private val ACCOUNT_CREATION_ERROR_EVENT = Any()

class RegisterPasswordViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val controller: SignUpController
) : BaseViewModel<RegisterPasswordViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val accountCreationError = PublishProcessor.create<Any>()

    init {
        runCommand(controller.setScreen(SignUpScreen.REGISTER_PASSWORD))

        query(controller.password().map(RegisterPasswordViewState::Password).take(1))
        query(controller.repeatedPassword().map(RegisterPasswordViewState::RepeatedPassword).take(1))

        query(
            merge(
                combineLatest(
                    controller.password(),
                    controller.isPasswordValid(),
                    { password, isPasswordValid -> !(password.isEmpty() || isPasswordValid) }
                ).debounce(PASSWORD_ERROR_DEBOUNCE, TimeUnit.MILLISECONDS),
                controller.password().map { false }
            ).map(RegisterPasswordViewState::PasswordError)
        )

        query(
            merge(
                combineLatest(
                    controller.repeatedPassword(),
                    controller.arePasswordsMatched(),
                    { repeatedPassword, arePasswordsMatched -> !(repeatedPassword.isEmpty() || arePasswordsMatched) }
                ).debounce(PASSWORD_ERROR_DEBOUNCE, TimeUnit.MILLISECONDS),
                controller.repeatedPassword().map { false }
            ).map(RegisterPasswordViewState::RepeatedPasswordError)
        )

        query(
            merge(
                accountCreationError.map { true },
                controller.password().map { false },
                controller.repeatedPassword().map { false }
            ).map(RegisterPasswordViewState::AccountCreationError)
        )
    }

    fun setPassword(password: String) = runCommand(controller.setPassword(password))

    fun setRepeatedPassword(repeatedPassword: String) = runCommand(controller.setRepeatedPassword(repeatedPassword))
}
