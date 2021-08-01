package com.toprest.home.ui.owner

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.reviewcardlib.model.ReviewItem

sealed class OwnerHomeViewState {
    data class Title(val title: String) : OwnerHomeViewState()
    data class Restaurants(val restaurants: List<RestaurantOverview>) : OwnerHomeViewState()
    data class PendingReviews(val reviews: List<ReviewItem>) : OwnerHomeViewState()
    data class ScreenType(val screenType: OwnerHomeScreenType) : OwnerHomeViewState()
}
