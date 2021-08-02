package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class EditReply(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<EditReply.Param> {

    override fun invoke(param: Param): Completable = restaurantSource.editReply(param.restaurantId, param.reviewId, param.reply)

    data class Param(val restaurantId: String, val reviewId: String, val reply: String)
}
