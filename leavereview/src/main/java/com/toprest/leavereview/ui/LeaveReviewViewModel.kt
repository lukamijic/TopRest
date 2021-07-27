package com.toprest.leavereview.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.usecase.LeaveReview
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class LeaveReviewViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val restaurantId: String,
    private val queryUser: QueryUser,
    private val leaveReview: LeaveReview
) : BaseViewModel<LeaveReviewViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val score = BehaviorProcessor.create<Int>()
    private val date = BehaviorProcessor.create<String>()
    private val review = BehaviorProcessor.createDefault("")

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(
            score.observeOn(backgroundScheduler)
                .map(LeaveReviewViewState::Score)
        )

        query(
            date.observeOn(backgroundScheduler)
                .map(LeaveReviewViewState::Date)
        )

        query(isLoading.observeOn(backgroundScheduler).map(LeaveReviewViewState::Loading))

        query(
            combineLatest(
                score.map { true }.startWithItem(false),
                date.map { true }.startWithItem(false),
                review.map { it.isNotBlank() },
                { isScoreValid, isDateValid, isReviewValid -> LeaveReviewViewState.ReviewButton(isScoreValid && isDateValid && isReviewValid) }
            )
        )
    }

    fun setScore(score: Int) = this.score.onNext(score)

    fun setDate(date: String) = this.date.onNext(date)

    fun setReview(review: String) = this.review.onNext(review)

    fun review() {
        isLoading.onNext(true)
        runCommand(
            combineLatest(
                queryUser(),
                score,
                date,
                review,
                { user, score, date, review -> LeaveReview.Param(restaurantId, user.id, review, score, date) }
            ).observeOn(backgroundScheduler)
                .firstOrError()
                .flatMapCompletable(leaveReview::invoke)
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeLeaveReview)
}
