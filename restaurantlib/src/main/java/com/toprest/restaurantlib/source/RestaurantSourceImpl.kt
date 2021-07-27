package com.toprest.restaurantlib.source

import com.toprest.restaurantlib.client.RestaurantClient
import io.reactivex.rxjava3.core.Completable

class RestaurantSourceImpl(
    private val client: RestaurantClient
) : RestaurantSource {

    override fun createRestaurant(ownerId: String, name: String, description: String): Completable =
        client.createRestaurant(ownerId, name, description)
            .flatMapCompletable { restaurantId -> client.storeRestaurantOwnership(ownerId, restaurantId) }
}
