package com.toprest.home.ui.admin

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.coreui.BaseViewModel
import com.toprest.home.ui.filter.controller.FilterController
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.usecase.QueryRestaurants
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class AdminHomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    filterController: FilterController,
    queryRestaurants: QueryRestaurants
) : BaseViewModel<AdminHomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val tab = BehaviorProcessor.createDefault(Tab.RESTAURANT)

    init {
        query(tab.observeOn(backgroundScheduler).map(AdminHomeViewState::SelectedTab))

        query(
            Flowable.combineLatest(
                queryRestaurants(),
                filterController.minimumScore(),
                { restaurants, minimumScore ->
                    restaurants.map(this::toRestaurantOverview).filter { filterMinimumScore(it.averageScore, minimumScore) }
                        .sortedByDescending { overview -> overview.averageScore }
                }
            )
                .map(AdminHomeViewState::Restaurants)
        )
    }

    fun setTab(tab: Tab) {
        this.tab.onNext(tab)
    }

    fun showRestaurantDetails(restaurantId: String) = dispatchRoutingAction { it.showRestaurantDetails(restaurantId)  }

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

enum class Tab {
    RESTAURANT, REVIEWS
}
