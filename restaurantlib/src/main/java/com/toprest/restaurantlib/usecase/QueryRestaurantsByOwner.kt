package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithParam
import com.toprest.restaurantlib.model.domain.Restaurant
import io.reactivex.rxjava3.core.Flowable

class QueryRestaurantsByOwner(private val queryRestaurants: QueryRestaurants) : QueryUseCaseWithParam<String, List<Restaurant>> {

    override fun invoke(param: String): Flowable<List<Restaurant>> =
        queryRestaurants()
            .map { it.filter { restaurant -> restaurant.ownerId == param } }
}
