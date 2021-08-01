package com.toprest.restaurantlib.model.response

import com.toprest.restaurantlib.model.api.ApiRestaurant
import com.toprest.restaurantlib.model.api.ApiReview
import com.toprest.restaurantlib.model.domain.Reply
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.model.domain.Review

data class RestaurantResponse(
    val apiRestaurant: ApiRestaurant,
    val apiReviews: List<ApiReview>
) {

    fun toRestaurant() = Restaurant(
        apiRestaurant.id!!,
        apiRestaurant.ownerId!!,
        apiRestaurant.name!!,
        apiRestaurant.description!!,
        apiReviews.map { apiReview ->
            Review(
                apiReview.id!!,
                apiReview.restaurantId!!,
                apiReview.reviewerId!!,
                apiReview.review!!,
                apiReview.score!!,
                apiReview.dateOfVisit!!,
                apiReview.creationTimestamp!!,
                apiReview.reply?.let {
                    Reply(it.reply!!, it.creationTimestamp!!)
                } ?: Reply.EMPTY
            )
        }
    )
}
