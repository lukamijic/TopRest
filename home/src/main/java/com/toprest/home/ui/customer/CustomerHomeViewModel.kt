package com.toprest.home.ui.customer

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.coreui.BaseViewModel
import com.toprest.home.ui.filter.controller.FilterController
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.usecase.QueryRestaurants
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import kotlin.math.min

class CustomerHomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryRestaurants: QueryRestaurants,
    filterController: FilterController
) : BaseViewModel<CustomerHomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        query(
            Flowable.combineLatest(
                queryRestaurants(),
                filterController.minimumScore(),
                { restaurants, minimumScore ->
                    restaurants.map(this::toRestaurantOverview).filter { filterMinimumScore(it.averageScore, minimumScore) }
                        .sortedByDescending { overview -> overview.averageScore }
                }
            )
                .map(::CustomerHomeViewState)
        )
    }

    fun openRestaurantDetails(restaurantId: String) = dispatchRoutingAction { it.showRestaurantDetails(restaurantId) }

    fun openFilter() = dispatchRoutingAction(Router::showFilterHome)

    private fun toRestaurantOverview(restaurant: Restaurant): RestaurantOverview {
        return RestaurantOverview(
            restaurant.id,
            restaurant.name,
            restaurant.description,
            if (restaurant.reviews.isEmpty()) null else restaurant.reviews.map { it.score }.average().toFloat()
        )
    }

    private fun filterMinimumScore(averageScore: Float?, minimumScore: Int) = if (averageScore == null) {
        minimumScore == 0
    } else {
        averageScore >= minimumScore
    }
}
