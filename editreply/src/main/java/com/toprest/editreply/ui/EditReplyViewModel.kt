package com.toprest.editreply.ui

import com.toprest.core.extension.shareReplayLatest
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Reply
import com.toprest.restaurantlib.usecase.DeleteReply
import com.toprest.restaurantlib.usecase.EditReply
import com.toprest.restaurantlib.usecase.QueryReply
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class EditReplyViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryReply: QueryReply,
    private val editReply: EditReply,
    private val deleteReply: DeleteReply,
    private val restaurantId: String,
    private val reviewId: String
) : BaseViewModel<EditReplyViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val reply = queryReply(QueryReply.Param(restaurantId, reviewId)).shareReplayLatest()

    private val replyText = BehaviorProcessor.createDefault("")

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(
            combineLatest(
                reply,
                replyText.observeOn(backgroundScheduler),
                ::isButtonEnabled
            ).map(EditReplyViewState::ReplyButton)
        )

        query(isLoading.observeOn(backgroundScheduler).map(EditReplyViewState::Loading))

        query(reply.map { EditReplyViewState.InitialData(it.reply) }.take(1))
    }

    fun setReply(replyText: String) = this.replyText.onNext(replyText)

    fun edit() {
        isLoading.onNext(true)
        runCommand(
            replyText.firstOrError()
                .flatMapCompletable {
                    editReply(EditReply.Param(restaurantId, reviewId, it))
                }
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun delete() {
        isLoading.onNext(true)
        runCommand(
            deleteReply(DeleteReply.Param(restaurantId, reviewId))
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction { it.closeEditReply() }

    private fun isButtonEnabled(reply: Reply, replyText: String) =
        if (replyText.isBlank()) {
            false
        } else {
            reply.reply != replyText
        }
}
