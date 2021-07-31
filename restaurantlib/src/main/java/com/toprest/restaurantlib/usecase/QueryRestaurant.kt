package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithParam
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Flowable

class QueryRestaurant(private val restaurantSource: RestaurantSource) : QueryUseCaseWithParam<String, Restaurant> {

    override fun invoke(param: String): Flowable<Restaurant> =
        restaurantSource.restaurants().map { it.find { restaurant -> restaurant.id == param } }
}
