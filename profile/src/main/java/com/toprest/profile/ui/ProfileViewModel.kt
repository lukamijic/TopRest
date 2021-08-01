package com.toprest.profile.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.usecase.Logout
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Scheduler

class ProfileViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryUser: QueryUser,
    private val logout: Logout
) : BaseViewModel<ProfileViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        query(
            queryUser()
                .map {
                    ProfileViewState(
                        it.firstName,
                        it.lastName,
                        it.email,
                        it.userType.name
                    )
                }
        )
    }

    fun logout() = runCommand(logout.invoke())
}
