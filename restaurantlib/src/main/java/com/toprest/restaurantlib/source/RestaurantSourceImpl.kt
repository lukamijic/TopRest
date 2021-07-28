package com.toprest.restaurantlib.source

import com.toprest.restaurantlib.client.RestaurantClient
import io.reactivex.rxjava3.core.Completable

class RestaurantSourceImpl(
    private val client: RestaurantClient
) : RestaurantSource {

    override fun createRestaurant(ownerId: String, name: String, description: String): Completable =
        client.createRestaurant(ownerId, name, description)
            .flatMapCompletable { restaurantId -> client.storeRestaurantOwnership(ownerId, restaurantId) }

    override fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable =
        client.leaveReview(restaurantId, reviewerId, review, score, dateOfVisit)

    override fun replyToReview(restaurantId: String, reviewId: String, reply: String): Completable =
        client.replyToReview(restaurantId, reviewId, reply)
}
