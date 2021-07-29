package com.toprest.restaurantlib.client

import com.toprest.restaurantlib.model.response.RestaurantResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface RestaurantClient {

    fun createRestaurant(ownerId: String, name: String, description: String): Single<String>

    fun storeRestaurantOwnership(ownerId: String, restaurantId: String): Completable

    fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable

    fun replyToReview(restaurantId: String, reviewId: String, reply: String): Completable

    fun getRestaurants() : Flowable<List<RestaurantResponse>>
}
