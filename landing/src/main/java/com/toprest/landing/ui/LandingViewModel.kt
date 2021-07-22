package com.toprest.landing.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

class LandingViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<Unit>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {


}
