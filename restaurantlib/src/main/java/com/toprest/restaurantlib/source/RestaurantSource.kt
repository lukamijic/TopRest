package com.toprest.restaurantlib.source

import io.reactivex.rxjava3.core.Completable

interface RestaurantSource {

    fun createRestaurant(ownerId: String, name: String, description: String) : Completable
}
