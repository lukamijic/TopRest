package com.toprest.activity.main

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.usecase.QueryIsSignedIn
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler

class MainViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryIsSignedIn: QueryIsSignedIn
) : BaseViewModel<Unit>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(
            queryIsSignedIn()
                .filter { !it }
                .flatMapCompletable { Completable.fromAction { dispatchRoutingAction(Router::showStartActivity) } }
        )
    }
}
