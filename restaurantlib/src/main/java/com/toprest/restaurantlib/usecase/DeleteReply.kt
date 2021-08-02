package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class DeleteReply(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<DeleteReply.Param> {

    override fun invoke(param: Param): Completable = restaurantSource.deleteReply(param.restaurantId, param.reviewId)

    data class Param(val restaurantId: String, val reviewId: String)
}
