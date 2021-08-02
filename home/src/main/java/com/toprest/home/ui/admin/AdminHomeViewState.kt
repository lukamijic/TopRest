package com.toprest.home.ui.admin

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.reviewcardlib.model.ReviewItem
import com.toprest.usercardlib.model.UserItem

sealed class AdminHomeViewState {
    data class Restaurants(val restaurants: List<RestaurantOverview>) : AdminHomeViewState()
    data class SelectedTab(val tab: Tab) : AdminHomeViewState()
    data class Users(val users: List<UserItem>) : AdminHomeViewState()
}
