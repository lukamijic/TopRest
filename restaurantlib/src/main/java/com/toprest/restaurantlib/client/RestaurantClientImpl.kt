package com.toprest.restaurantlib.client

import com.google.firebase.database.DatabaseReference
import com.toprest.firebaselib.client.FirebaseClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler

class RestaurantClientImpl(
    private val database: DatabaseReference,
    backgroundScheduler: Scheduler
) : RestaurantClient, FirebaseClient(backgroundScheduler) {

    override fun createRestaurant(ownerId: String, name: String, description: String) : Completable = Completable.fromAction {  }
}
