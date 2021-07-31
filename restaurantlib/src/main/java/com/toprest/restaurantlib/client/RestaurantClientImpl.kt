package com.toprest.restaurantlib.client

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.toprest.firebaselib.client.FirebaseClient
import com.toprest.restaurantlib.model.api.ApiReply
import com.toprest.restaurantlib.model.api.ApiRestaurant
import com.toprest.restaurantlib.model.api.ApiReview
import com.toprest.restaurantlib.model.response.RestaurantResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

private const val RESTAURANTS_NODE = "restaurants"
private const val OWNERS_NODE = "owners"
private const val REVIEWS_NODE = "reviews"
private const val REPLY_NODE = "reply"

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

    override fun replyToReview(restaurantId: String, reviewId: String, reply: String) : Completable =
        database
            .child(RESTAURANTS_NODE)
            .child(restaurantId)
            .child(REVIEWS_NODE)
            .child(reviewId)
            .child(REPLY_NODE)
            .setValue(ApiReply(reply, System.currentTimeMillis()))
            .execute()

    override fun queryRestaurants(): Flowable<List<RestaurantResponse>> =
        database
            .child(RESTAURANTS_NODE)
            .query { dataSnapshot ->
                dataSnapshot.children.map { restaurantSnapshot ->
                    RestaurantResponse(
                        restaurantSnapshot.getValue<ApiRestaurant>()!!,
                        restaurantSnapshot.child(REVIEWS_NODE).children.map { it.getValue<ApiReview>()!! }
                    )
                }
            }

}
