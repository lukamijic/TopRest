package com.toprest.home.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

class HomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<HomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    fun openAddRestaurant() = dispatchRoutingAction(Router::showAddRestaurant)

    fun openLeaveReview() = dispatchRoutingAction { it.showLeaveReview("-MfeAjA5G3WYbMS07Kc5") }

    fun openReplyReview() = dispatchRoutingAction { it.showReviewReply("-MfeAjA5G3WYbMS07Kc5", "-MfeB_xYBSKtng7tf2CP") }
}
