package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class EditRestaurant(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<EditRestaurant.Param> {

    override fun invoke(param: Param): Completable = restaurantSource.editRestaurant(param.restaurantId, param.name, param.description)

    data class Param(val restaurantId: String, val name: String, val description: String)
}
