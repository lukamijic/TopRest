package com.toprest.editrestaurant.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.editrestaurant.ui.EditRestaurantViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun editRestaurantModule() : Module = module {

    viewModel { (restaurantId: String) ->
        EditRestaurantViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryRestaurant = get(),
            editRestaurant = get(),
            deleteRestaurant = get(),
            restaurantId = restaurantId
        )
    }
}
