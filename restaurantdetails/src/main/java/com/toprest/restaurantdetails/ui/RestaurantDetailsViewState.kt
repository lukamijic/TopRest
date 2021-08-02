package com.toprest.restaurantdetails.ui

import com.toprest.reviewcardlib.model.ReviewItem

sealed class RestaurantDetailsViewState {
    data class Details(val name: String, val description: String, val score: String) : RestaurantDetailsViewState()
    data class ReviewButton(val isVisible: Boolean) : RestaurantDetailsViewState()
    data class Reviews(val reviewsItems: List<ReviewItem>) : RestaurantDetailsViewState()
    data class Editable(val isEditable: Boolean) : RestaurantDetailsViewState()
}
