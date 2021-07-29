package com.toprest.profile.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.usecase.Logout
import io.reactivex.rxjava3.core.Scheduler

class ProfileViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val logout: Logout
) : BaseViewModel<ProfileViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    fun logout() = runCommand(logout.invoke())

    fun openAddRestaurant() = dispatchRoutingAction(Router::showAddRestaurant)

    fun openLeaveReview() = dispatchRoutingAction { it.showLeaveReview("-MfoYoaTZcnYLa2r26Lq") }
}
