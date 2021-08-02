package com.toprest.restaurantlib.source

import com.toprest.core.extension.shareReplayLatest
import com.toprest.restaurantlib.client.RestaurantClient
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.model.response.RestaurantResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class RestaurantSourceImpl(
    private val client: RestaurantClient
) : RestaurantSource {

    private val restaurants = client.queryRestaurants()
        .map { it.map(RestaurantResponse::toRestaurant) }
        .shareReplayLatest()

    override fun createRestaurant(ownerId: String, name: String, description: String): Completable =
        client.createRestaurant(ownerId, name, description)

    override fun editRestaurant(restaurantId: String, restaurantName: String, restaurantDescription: String): Completable =
        client.editRestaurant(restaurantId, restaurantName, restaurantDescription)

    override fun deleteRestaurant(restaurantId: String): Completable = client.deleteRestaurant(restaurantId)

    override fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable =
        client.leaveReview(restaurantId, reviewerId, review, score, dateOfVisit)

    override fun editReview(restaurantId: String, reviewId: String, review: String, score: Int, dateOfVisit: String): Completable =
        client.editReview(restaurantId, reviewId, review, score, dateOfVisit)

    override fun deleteReview(restaurantId: String, reviewId: String): Completable = client.deleteReview(restaurantId, reviewId)

    override fun replyToReview(restaurantId: String, reviewId: String, reply: String): Completable =
        client.replyToReview(restaurantId, reviewId, reply)

    override fun editReply(restaurantId: String, reviewId: String, reply: String): Completable =
        client.editReply(restaurantId, reviewId, reply)

    override fun deleteReply(restaurantId: String, reviewId: String): Completable = client.deleteReply(restaurantId, reviewId)

    override fun restaurants(): Flowable<List<Restaurant>> = restaurants
}
