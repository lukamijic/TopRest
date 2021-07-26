package com.toprest.dashboard.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class DashboardViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<DashboardViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val positionProcessor = BehaviorProcessor.create<Int>()

    init {
        query(
            positionProcessor
                .observeOn(backgroundScheduler)
                .map(::DashboardViewState)
        )
    }

    fun positionSelected(position: Int) {
        positionProcessor.onNext(position)
    }
}
