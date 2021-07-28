package com.tempo.reply.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.usecase.ReplyToReview
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class ReviewReplyViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val restaurantId: String,
    private val reviewId: String,
    private val replyToReview: ReplyToReview
) : BaseViewModel<ReviewReplyViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val reply = BehaviorProcessor.createDefault("")

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(reply.observeOn(backgroundScheduler).map(String::isNotEmpty).map(ReviewReplyViewState::ReplyButton))

        query(isLoading.observeOn(backgroundScheduler).map(ReviewReplyViewState::Loading))
    }

    fun setReply(reply: String) = this.reply.onNext(reply)

    fun reply() {
        isLoading.onNext(true)
        runCommand(
            reply
                .firstOrError()
                .flatMapCompletable { reply -> replyToReview(ReplyToReview.Param(restaurantId, reviewId, reply)) }
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeReviewReply)
}
