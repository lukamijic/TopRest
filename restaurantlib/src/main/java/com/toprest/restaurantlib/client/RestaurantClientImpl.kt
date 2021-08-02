package com.toprest.restaurantlib.client

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

private const val RESTAURANTS_NODE = "restaurants"
private const val RESTAURANT_NAME_NODE = "name"
private const val RESTAURANT_NAME_DESCRIPTION_NODE = "description"

private const val REVIEWS_NODE = "reviews"
private const val REVIEWS_REVIEW_NODE = "review"
private const val REVIEWS_SCORE_NODE = "score"
private const val REVIEWS_DATE_OF_VISIT_NODE = "dateOfVisit"

private const val REPLY_NODE = "reply"
private const val REPLY_REPLY_NODE = "reply"

class RestaurantClientImpl(
    private val database: DatabaseReference,
    backgroundScheduler: Scheduler
) : RestaurantClient, FirebaseClient(backgroundScheduler) {

    override fun createRestaurant(ownerId: String, name: String, description: String): Completable {
        val newRestaurantNode = database.child(RESTAURANTS_NODE).push()
        val restaurantId = newRestaurantNode.key

        return newRestaurantNode.setValue(ApiRestaurant(restaurantId, ownerId, name, description))
            .execute()
    }

    override fun editRestaurant(restaurantId: String, restaurantName: String, restaurantDescription: String) : Completable {
        val restaurantNode = database.child(RESTAURANTS_NODE).child(restaurantId)
        val updateFields = mapOf(
            RESTAURANT_NAME_NODE to restaurantName,
            RESTAURANT_NAME_DESCRIPTION_NODE to restaurantDescription
        )

        return restaurantNode.updateChildren(updateFields).execute()
    }

    override fun deleteRestaurant(restaurantId: String): Completable =
        database
            .child(RESTAURANTS_NODE)
            .child(restaurantId)
            .removeValue()
            .execute()

    override fun leaveReview(restaurantId: String, reviewerId: String, review: String, score: Int, dateOfVisit: String): Completable {
        val newReviewNode = database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).push()
        val reviewId = newReviewNode.key

        return newReviewNode.setValue(ApiReview(reviewId, restaurantId, reviewerId, review, score, dateOfVisit, System.currentTimeMillis()))
            .execute()
    }

    override fun editReview(restaurantId: String, reviewId: String, review: String, score: Int, dateOfVisit: String): Completable {
        val reviewNode = database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).child(reviewId)
        val updateFields = mapOf(
            REVIEWS_REVIEW_NODE to review,
            REVIEWS_SCORE_NODE to score,
            REVIEWS_DATE_OF_VISIT_NODE to dateOfVisit
        )

        return reviewNode.updateChildren(updateFields).execute()
    }

    override fun deleteReview(restaurantId: String, reviewId: String): Completable =
        database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).child(reviewId).removeValue().execute()

    override fun replyToReview(restaurantId: String, reviewId: String, reply: String) : Completable =
        database
            .child(RESTAURANTS_NODE)
            .child(restaurantId)
            .child(REVIEWS_NODE)
            .child(reviewId)
            .child(REPLY_NODE)
            .setValue(ApiReply(reply, System.currentTimeMillis()))
            .execute()

    override fun editReply(restaurantId: String, reviewId: String, reply: String): Completable {
        val replyNode = database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).child(reviewId).child(REPLY_NODE)
        val updateField = mapOf(REPLY_REPLY_NODE to reply)

        return replyNode.updateChildren(updateField).execute()
    }

    override fun deleteReply(restaurantId: String, reviewId: String) : Completable =
        database.child(RESTAURANTS_NODE).child(restaurantId).child(REVIEWS_NODE).child(reviewId).child(REPLY_NODE).removeValue().execute()


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
