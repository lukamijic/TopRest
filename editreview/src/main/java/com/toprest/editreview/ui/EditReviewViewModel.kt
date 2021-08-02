package com.toprest.editreview.ui

import com.toprest.core.extension.shareReplayLatest
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Review
import com.toprest.restaurantlib.usecase.DeleteReview
import com.toprest.restaurantlib.usecase.EditReview
import com.toprest.restaurantlib.usecase.QueryReview
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class EditReviewViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryReview: QueryReview,
    private val editReview: EditReview,
    private val deleteReview: DeleteReview,
    private val restaurantId: String,
    private val reviewId: String
) : BaseViewModel<EditReviewViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val review = queryReview(QueryReview.Param(restaurantId, reviewId)).shareReplayLatest()

    private val reviewText = BehaviorProcessor.createDefault("")
    private val score = BehaviorProcessor.create<Int>()
    private val date = BehaviorProcessor.create<String>()

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(
            combineLatest(
                review,
                reviewText.observeOn(backgroundScheduler),
                score.observeOn(backgroundScheduler),
                date.observeOn(backgroundScheduler),
                ::isButtonEnabled
            ).map(EditReviewViewState::ReviewButton)
        )

        query(isLoading.observeOn(backgroundScheduler).map(EditReviewViewState::Loading))

        query(score.observeOn(backgroundScheduler).map(EditReviewViewState::Score))

        query(date.observeOn(backgroundScheduler).map(EditReviewViewState::Date))

        query(review.map { EditReviewViewState.InitialData(it.review, it.score, it.dateOfVisit) }.take(1))
    }

    fun setScore(score: Int) = this.score.onNext(score)

    fun setDate(date: String) = this.date.onNext(date)

    fun setReview(review: String) = reviewText.onNext(review)

    fun edit() {
        isLoading.onNext(true)
        runCommand(
            combineLatest(
                reviewText,
                score,
                date,
                { reviewText, score, date -> EditReview.Param(restaurantId, reviewId, reviewText, score, date) }
            ).firstOrError()
                .flatMapCompletable(editReview::invoke)
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun delete() {
        isLoading.onNext(true)
        runCommand(
            deleteReview(DeleteReview.Param(restaurantId, reviewId))
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeEditReview)

    private fun isButtonEnabled(review: Review, reviewText: String, reviewScore: Int, dateOfVisit: String) =
        if (reviewText.isBlank() || dateOfVisit.isBlank()) {
            false
        } else {
            review.review != reviewText || review.score != reviewScore || review.dateOfVisit != dateOfVisit
        }
}
