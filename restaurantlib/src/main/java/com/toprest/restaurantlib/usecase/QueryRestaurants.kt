package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCase
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.source.RestaurantSource
import io.reactivex.rxjava3.core.Flowable

class QueryRestaurants(private val restaurantSource: RestaurantSource) : QueryUseCase<List<Restaurant>> {

    override fun invoke(): Flowable<List<Restaurant>> = restaurantSource.restaurants()
}
