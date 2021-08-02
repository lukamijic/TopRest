package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class EditReview(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<EditReview.Param> {

    override fun invoke(param: Param): Completable = with(param) {
        restaurantSource.editReview(restaurantId, reviewId, review, score, dateOfVisit)
    }

    data class Param(val restaurantId: String, val reviewId: String, val review: String, val score: Int, val dateOfVisit: String)
}
