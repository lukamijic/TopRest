package com.toprest.home.ui.customer

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.usecase.QueryRestaurants
import io.reactivex.rxjava3.core.Scheduler

class CustomerHomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryRestaurants: QueryRestaurants
) : BaseViewModel<CustomerHomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        query(
            queryRestaurants()
                .map { it.map(this::toRestaurantOverview).sortedByDescending { overview -> overview.averageScore } }
                .map(::CustomerHomeViewState)
        )
    }

    fun openRestaurantDetails(restaurantId: String) {

    }

    private fun toRestaurantOverview(restaurant: Restaurant): RestaurantOverview {
        return RestaurantOverview(
            restaurant.id,
            restaurant.name,
            restaurant.description,
            if (restaurant.reviews.isEmpty()) null else restaurant.reviews.map { it.score }.average().toFloat()
        )
    }
}
