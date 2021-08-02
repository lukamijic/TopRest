package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Completable

class DeleteRestaurant(private val restaurantSource: RestaurantSource) : CommandUseCaseWithParam<String> {

    override fun invoke(param: String): Completable = restaurantSource.deleteRestaurant(param)
}
