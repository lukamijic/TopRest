package com.toprest.restaurantlib.client

import io.reactivex.rxjava3.core.Completable

interface RestaurantClient {

    fun createRestaurant(ownerId: String, name: String, description: String) : Completable
}
