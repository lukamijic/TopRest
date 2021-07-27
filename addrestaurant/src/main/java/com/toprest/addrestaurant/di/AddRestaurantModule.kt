package com.toprest.addrestaurant.di

import com.toprest.addrestaurant.ui.AddRestaurantViewModel
import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun addRestaurantModule() : Module = module {

    viewModel {
        AddRestaurantViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUser = get(),
            createRestaurant = get()
        )
    }
}
