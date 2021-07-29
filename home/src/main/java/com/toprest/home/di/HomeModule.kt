package com.toprest.home.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.home.ui.HomeViewModel
import com.toprest.home.ui.customer.CustomerHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun homeModule() : Module = module {

    viewModel {
        HomeViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUser = get()
        )
    }

    viewModel {
        CustomerHomeViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryRestaurants = get()
        )
    }
}
