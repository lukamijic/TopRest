package com.toprest.restaurantdetails.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.restaurantdetails.ui.RestaurantDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun restaurantDetailsModule() : Module = module {

    viewModel { (restaurantId: String) ->
        RestaurantDetailsViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUser = get(),
            queryRestaurant = get(),
            restaurantId = restaurantId
        )
    }
}
