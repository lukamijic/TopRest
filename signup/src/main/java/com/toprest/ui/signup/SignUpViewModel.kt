package com.toprest.ui.signup

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseScopeClosingViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.scope.SignUpScopeHolder
import io.reactivex.rxjava3.core.Scheduler

class SignUpViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    signUpScopeHolder: SignUpScopeHolder,
    private val controller: SignUpController
) : BaseScopeClosingViewModel<SignUpViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher,
    signUpScopeHolder
) {

    init {
        query(controller.title().map(SignUpViewState::Title))

        query(controller.actionButtonEnabled().distinctUntilChanged().map(SignUpViewState::ActionButtonEnabled))

        query(controller.actionButtonText().map(SignUpViewState::ActionButtonText))

        query(controller.loading().map(SignUpViewState::Loading))

        dispatchRoutingAction(Router::showRegisterName)
    }

    fun actionButtonClicked() = runCommand(controller.actionButtonClicked())

    fun previous() = runCommand(controller.previous())
}
