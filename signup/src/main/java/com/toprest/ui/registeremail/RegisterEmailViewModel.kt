package com.toprest.ui.registeremail

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseViewModel
import com.toprest.model.SignUpScreen
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit

private const val EMAIL_ERROR_DEBOUNCE = 1000L

class RegisterEmailViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val controller: SignUpController
) : BaseViewModel<RegisterEmailViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(controller.setScreen(SignUpScreen.REGISTER_EMAIL))

        query(controller.email().map(RegisterEmailViewState::Email).take(1))

        query(
            merge(
                combineLatest(
                    controller.email(),
                    controller.isEmailValid(),
                    { email, isValid -> !(email.isEmpty() || isValid)}
                ).debounce(EMAIL_ERROR_DEBOUNCE, TimeUnit.MILLISECONDS),
                controller.email().map { false }
            ).map(RegisterEmailViewState::Error)
        )
    }

    fun setEmail(email: String) = runCommand(controller.setEmail(email))
}
