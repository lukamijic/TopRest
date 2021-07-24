package com.toprest.ui.usertype

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseViewModel
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.model.SignUpScreen
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

class UserTypeSelectionViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val controller: SignUpController
) : BaseViewModel<UserTypeSelectionViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(controller.setScreen(SignUpScreen.SELECT_ACCOUNT_TYPE))

        query(controller.userType().map(::UserTypeSelectionViewState).take(1))
    }

    fun setUserType(userType: UserType) = runCommand(controller.setUserType(userType))
}
