package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class CreateRestaurant(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<CreateRestaurant.Param> {

    override fun invoke(param: Param): Completable = restaurantSource.createRestaurant(param.ownerId, param.name, param.description)

    data class Param(val ownerId: String, val name: String, val description: String)
}
