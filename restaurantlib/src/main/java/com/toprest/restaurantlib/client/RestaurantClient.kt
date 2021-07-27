package com.toprest.restaurantlib.client

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface RestaurantClient {

    fun createRestaurant(ownerId: String, name: String, description: String) : Single<String>

    fun storeRestaurantOwnership(ownerId: String, restaurantId: String): Completable
}
