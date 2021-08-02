package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithParam
import com.toprest.restaurantlib.model.domain.Reply
import io.reactivex.rxjava3.core.Flowable

class QueryReply(private val queryReview: QueryReview) : QueryUseCaseWithParam<QueryReply.Param, Reply> {

    override fun invoke(param: Param): Flowable<Reply> = queryReview(QueryReview.Param(param.restaurantId, param.reviewId))
        .map { it.reply }

    data class Param(val restaurantId: String, val reviewId: String)
}
