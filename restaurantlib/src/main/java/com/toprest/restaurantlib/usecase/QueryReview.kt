package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithParam
import com.toprest.restaurantlib.model.domain.Review
import io.reactivex.rxjava3.core.Flowable

class QueryReview(private val queryRestaurant: QueryRestaurant) : QueryUseCaseWithParam<QueryReview.Param, Review> {

    override fun invoke(param: Param): Flowable<Review> = queryRestaurant(param.restaurantId)
        .map { it.reviews.find { review -> review.id == param.reviewId } ?: Review.EMPTY }

    data class Param(val restaurantId: String, val reviewId: String)
}
