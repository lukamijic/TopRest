package com.toprest.home.ui.admin

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.reviewcardlib.model.ReviewItem

sealed class AdminHomeViewState {
    data class Restaurants(val restaurants: List<RestaurantOverview>) : AdminHomeViewState()
    data class Reviews(val reviews: List<ReviewItem>) : AdminHomeViewState()
    data class SelectedTab(val tab: Tab) : AdminHomeViewState()
}
