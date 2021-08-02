package com.toprest.home.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Scheduler

class HomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val queryUser: QueryUser
) : BaseViewModel<HomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        query(
            queryUser()
                .map(User::userType)
                .distinctUntilChanged()
                .map(::HomeViewState)
        )
    }
}
