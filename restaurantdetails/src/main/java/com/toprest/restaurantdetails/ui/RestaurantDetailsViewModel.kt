package com.toprest.restaurantdetails.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.model.domain.Review
import com.toprest.restaurantlib.usecase.QueryRestaurant
import com.toprest.reviewcardlib.model.ReplyItem
import com.toprest.reviewcardlib.model.ReviewItem
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

class RestaurantDetailsViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryUser: QueryUser,
    queryRestaurant: QueryRestaurant,
    private val restaurantId: String
) : BaseViewModel<RestaurantDetailsViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val user = queryUser()

    private val restaurant = queryRestaurant(restaurantId)

    init {
        query(
            restaurant
                .map { RestaurantDetailsViewState.Details(it.name, it.description, averageScore(it.reviews)) }
        )

        query(
            combineLatest(
                user,
                restaurant,
                BiFunction(::toReviewButtonViewState)
            )
        )

        query(
            combineLatest(
                user,
                restaurant,
                BiFunction(::toReviewsViewState)
            )
        )

        query(user.map { RestaurantDetailsViewState.Editable(it.userType == UserType.ADMIN) })
    }

    fun reply(reviewId: String) = dispatchRoutingAction { it.showReviewReply(restaurantId, reviewId) }

    fun leaveReview() = dispatchRoutingAction { it.showLeaveReview(restaurantId) }

    fun showEditRestaurant() = dispatchRoutingAction { it.showEditRestaurant(restaurantId) }

    fun showEditReview(reviewId: String) = dispatchRoutingAction { it.showEditReview(restaurantId, reviewId) }

    fun showEditReply(reviewId: String) = dispatchRoutingAction { it.showEditReply(restaurantId, reviewId) }

    private fun averageScore(reviews: List<Review>) =
        if (reviews.isEmpty()) "N/A" else String.format("%.1f/5", reviews.map { it.score }.average().toFloat())

    private fun toReviewButtonViewState(user: User, restaurant: Restaurant) =
        if (user.id == restaurant.ownerId) {
            RestaurantDetailsViewState.ReviewButton(false)
        } else {
            RestaurantDetailsViewState.ReviewButton(!restaurant.reviews.map { it.reviewerId }.contains(user.id))
        }

    private fun toReviewsViewState(user: User, restaurant: Restaurant) = RestaurantDetailsViewState.Reviews(
        restaurant.reviews.sortedByDescending { it.score }.let {
            if (it.size < 3) {
                it
            } else {
                val mutableReviews = it.toMutableList()
                val bestRated = mutableReviews.removeFirst()
                val worstRated = mutableReviews.removeLast()
                listOf(bestRated, worstRated) + mutableReviews.sortedByDescending { review -> dateFormat.parse(review.dateOfVisit)!!.time }
            }
        }
            .map {
                ReviewItem(
                    it.id,
                    restaurantId,
                    it.score,
                    it.dateOfVisit,
                    it.review,
                    user.id == restaurant.ownerId && it.reply.isEmpty(),
                    if (it.reply.isEmpty()) null else ReplyItem(it.reply.reply),
                    user.userType == UserType.ADMIN
                )
            }
    )
}
