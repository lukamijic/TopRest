package com.toprest.restaurantlib.client

import com.google.firebase.database.DatabaseReference
import com.toprest.firebaselib.client.FirebaseClient
import com.toprest.restaurantlib.model.api.ApiRestaurant
import com.toprest.restaurantlib.model.api.ApiReview
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

private const val RESTAURANTS_NODE = "restaurants"
private const val OWNERS_NODE = "owners"
private const val REVIEWS_NODE = "reviews"

class RestaurantClientImpl(
    private val database: DatabaseReference,
    backgroundScheduler: Scheduler
) : RestaurantClient, FirebaseClient(backgroundScheduler) {

    override fun createRestaurant(ownerId: String, name: String, description: String): Single<String> {
        val newRestaurantNode = database.child(RESTAURANTS_NODE).push()
        val restaurantId = newRestaurantNode.key

        return newRestaurantNode.setValue(ApiRestaurant(restaurantId, ownerId, name, description))
            .get { restaurantId }
    }

    override fun storeRestaurantOwnership(ownerId: String, restaurantId: String): Completable =
        database
            .child(OWNERS_NODE)
            .child(ownerId)
            .push()
            .setValue(restaurantId)
            .execute()

    override fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable {
        val newReviewNode = database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).push()
        val reviewId = newReviewNode.key

        return newReviewNode.setValue(ApiReview(reviewId, reviewerId, review, score, dateOfVisit, System.currentTimeMillis()))
            .execute()
    }
}