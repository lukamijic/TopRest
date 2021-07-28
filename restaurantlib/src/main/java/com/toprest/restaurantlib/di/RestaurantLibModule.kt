package com.toprest.restaurantlib.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.restaurantlib.client.RestaurantClient
import com.toprest.restaurantlib.client.RestaurantClientImpl
import com.toprest.restaurantlib.source.RestaurantSource
import com.toprest.restaurantlib.source.RestaurantSourceImpl
import com.toprest.restaurantlib.usecase.CreateRestaurant
import com.toprest.restaurantlib.usecase.LeaveReview
import com.toprest.restaurantlib.usecase.ReplyToReview
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun restaurantLibModule() : Module = module {

    single<RestaurantClient> {
        RestaurantClientImpl(
            database = get(),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
        )
    }

    single<RestaurantSource> { RestaurantSourceImpl(get()) }

    single { CreateRestaurant(get()) }

    single { LeaveReview(get()) }

    single { ReplyToReview(get()) }
}
