package com.toprest.home.ui.owner

import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.core.extension.toCompletable
import com.toprest.coreui.BaseViewModel
import com.toprest.home.ui.owner.translations.OwnerHomeTranslations
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Review
import com.toprest.restaurantlib.usecase.QueryOwnerPendingReviews
import com.toprest.restaurantlib.usecase.QueryRestaurantsByOwner
import com.toprest.reviewcardlib.model.ReviewItem
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class OwnerHomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryUser: QueryUser,
    queryRestaurantsByOwner: QueryRestaurantsByOwner,
    queryOwnerPendingReviews: QueryOwnerPendingReviews,
    private val translations: OwnerHomeTranslations
) : BaseViewModel<OwnerHomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val screenType = BehaviorProcessor.createDefault(OwnerHomeScreenType.RESTAURANTS)
    private val ownerId = queryUser().map { it.id }

    private val pendingReviews = queryUser().switchMap { queryOwnerPendingReviews(it.id) }

    init {
        query(screenType.observeOn(backgroundScheduler).map(OwnerHomeViewState::ScreenType))

        query(
            screenType
                .map { screenType ->
                    when (screenType!!) {
                        OwnerHomeScreenType.RESTAURANTS -> translations.restaurantsTitles()
                        OwnerHomeScreenType.REVIEWS -> translations.reviewsTitle()
                    }
                }.map(OwnerHomeViewState::Title)
        )

        query(
            ownerId
                .switchMap(queryRestaurantsByOwner::invoke)
                .map { restaurants ->
                    restaurants.map {
                        RestaurantOverview(
                            it.id,
                            it.name,
                            it.description,
                            averageScore(it.reviews)
                        )
                    }
                }.map { it.sortedByDescending { restaurant -> restaurant.averageScore } }
                .map(OwnerHomeViewState::Restaurants)
        )

        query(
            pendingReviews
                .map { reviews ->
                    reviews.map {
                        ReviewItem(
                            it.id,
                            it.restaurantId,
                            it.score,
                            it.dateOfVisit,
                            it.review,
                            true,
                            null
                        )
                    }.sortedBy { it.score }
                }.map(OwnerHomeViewState::PendingReviews)
        )
    }

    fun showRestaurantDetails(restaurantId: String) = dispatchRoutingAction { it.showRestaurantDetails(restaurantId) }

    fun showReplyReview(reviewId: String) = runCommand(
        pendingReviews
            .map { it.find { review -> review.id == reviewId }!!.restaurantId }
            .firstOrError()
            .toCompletable { restaurantId -> dispatchRoutingAction { it.showReviewReply(restaurantId, reviewId) } }
    )

    fun showAddRestaurant() = dispatchRoutingAction(Router::showAddRestaurant)

    fun changeScreenType() = runCommand(
        screenType
            .firstOrError()
            .toCompletable {
                when(it!!) {
                    OwnerHomeScreenType.RESTAURANTS -> screenType.onNext(OwnerHomeScreenType.REVIEWS)
                    OwnerHomeScreenType.REVIEWS -> screenType.onNext(OwnerHomeScreenType.RESTAURANTS)
                }
            }
    )

    private fun averageScore(reviews: List<Review>) =
        if (reviews.isEmpty()) null else reviews.map { it.score }.average().toFloat()
}

enum class OwnerHomeScreenType {
    RESTAURANTS, REVIEWS
}
