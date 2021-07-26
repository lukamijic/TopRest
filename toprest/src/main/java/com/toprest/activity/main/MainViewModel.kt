package com.toprest.activity.main

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler

class MainViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryUser: QueryUser
) : BaseViewModel<Unit>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(
            queryUser()
                .filter(User::isEmpty)
                .flatMapCompletable { Completable.fromAction { dispatchRoutingAction(Router::showStartActivity) } }
        )
    }
}
