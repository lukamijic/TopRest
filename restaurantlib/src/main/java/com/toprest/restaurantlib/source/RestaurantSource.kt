package com.toprest.restaurantlib.source

import io.reactivex.rxjava3.core.Completable

interface RestaurantSource {

    fun createRestaurant(ownerId: String, name: String, description: String) : Completable

    fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String) : Completable
}
