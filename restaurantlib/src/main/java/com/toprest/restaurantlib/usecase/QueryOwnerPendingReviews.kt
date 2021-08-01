package com.toprest.restaurantlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithCachedParam
import com.toprest.restaurantlib.model.domain.Reply
import com.toprest.restaurantlib.model.domain.Review
import io.reactivex.rxjava3.core.Flowable

class QueryOwnerPendingReviews(private val queryRestaurantsByOwner: QueryRestaurantsByOwner) : QueryUseCaseWithCachedParam<String, List<Review>>() {

    override fun createQuery(param: String): Flowable<List<Review>> = queryRestaurantsByOwner(param)
        .map { restaurants ->
            restaurants
                .map { it.reviews }
                .flatten()
                .filter { it.reply == Reply.EMPTY }
        }
}
