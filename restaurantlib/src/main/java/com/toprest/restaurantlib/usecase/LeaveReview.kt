package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class LeaveReview(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<LeaveReview.Param> {

    override fun invoke(param: Param): Completable = restaurantSource.leaveReview(
        param.restaurantId,
        param.reviewerId,
        param.review,
        param.score,
        param.dateOfVisit
    )

    data class Param(val restaurantId: String, val reviewerId: String, val review: String, val score: Int, val dateOfVisit: String)
}
