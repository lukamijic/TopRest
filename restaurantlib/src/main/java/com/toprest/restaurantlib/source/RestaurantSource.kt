package com.toprest.restaurantlib.source

import com.toprest.restaurantlib.model.domain.Restaurant
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface RestaurantSource {

    fun createRestaurant(ownerId: String, name: String, description: String) : Completable

    fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String) : Completable

    fun replyToReview(restaurantId: String, reviewId: String, reply: String) : Completable

    fun restaurants(): Flowable<List<Restaurant>>
}
