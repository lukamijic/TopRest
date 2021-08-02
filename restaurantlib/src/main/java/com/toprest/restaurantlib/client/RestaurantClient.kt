package com.toprest.restaurantlib.client

import com.toprest.restaurantlib.model.response.RestaurantResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface RestaurantClient {

    fun createRestaurant(ownerId: String, name: String, description: String): Completable

    fun editRestaurant(restaurantId: String, restaurantName: String, restaurantDescription: String) : Completable

    fun deleteRestaurant(restaurantId: String) : Completable

    fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable

    fun editReview(restaurantId: String, reviewId: String, review: String, score: Int, dateOfVisit: String) : Completable

    fun deleteReview(restaurantId: String, reviewId: String) : Completable

    fun replyToReview(restaurantId: String, reviewId: String, reply: String): Completable

    fun editReply(restaurantId: String, reviewId: String, reply: String) : Completable

    fun deleteReply(restaurantId: String, reviewId: String) : Completable

    fun queryRestaurants() : Flowable<List<RestaurantResponse>>
}
