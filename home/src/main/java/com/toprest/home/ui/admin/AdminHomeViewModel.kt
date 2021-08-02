package com.toprest.home.ui.admin

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.coreui.BaseViewModel
import com.toprest.home.ui.filter.controller.FilterController
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.usecase.QueryRestaurants
import com.toprest.sessionlib.usecase.QueryUsers
import com.toprest.usercardlib.model.UserItem
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class AdminHomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    filterController: FilterController,
    queryRestaurants: QueryRestaurants,
    queryUsers: QueryUsers
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

        query(queryUsers().map { it.map { user -> UserItem(user.id, user.email) } }.map(AdminHomeViewState::Users))
    }

    fun setTab(tab: Tab) {
        this.tab.onNext(tab)
    }

    fun showRestaurantDetails(restaurantId: String) = dispatchRoutingAction { it.showRestaurantDetails(restaurantId)  }

    fun openFilter() = dispatchRoutingAction(Router::showFilterHome)

    fun showEditUser(userId: String) = dispatchRoutingAction { it.showEditUser(userId) }

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
    RESTAURANT, USERS
}
