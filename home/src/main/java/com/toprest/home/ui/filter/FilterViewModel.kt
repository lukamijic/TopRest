package com.toprest.home.ui.filter

import com.toprest.coreui.BaseViewModel
import com.toprest.home.ui.filter.controller.FilterController
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

class FilterViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val filterController: FilterController
) : BaseViewModel<FilterViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        query(filterController.minimumScore().map(::FilterViewState))
    }

    fun setMinimumScore(score: Int) = runCommand(filterController.setMinimumScore(score))

    fun close() = dispatchRoutingAction(Router::closeFilterHome)
}
