package com.toprest.home.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.home.ui.HomeViewModel
import com.toprest.home.ui.admin.AdminHomeViewModel
import com.toprest.home.ui.customer.CustomerHomeViewModel
import com.toprest.home.ui.filter.FilterViewModel
import com.toprest.home.ui.filter.controller.FilterController
import com.toprest.home.ui.filter.controller.FilterControllerImpl
import com.toprest.home.ui.owner.OwnerHomeViewModel
import com.toprest.home.ui.owner.translations.OwnerHomeTranslations
import com.toprest.home.ui.owner.translations.OwnerHomeTranslationsImpl
import org.koin.android.ext.koin.androidContext
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
            queryRestaurants = get(),
            filterController = get()
        )
    }

    viewModel {
        OwnerHomeViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUser = get(),
            queryRestaurantsByOwner = get(),
            queryOwnerPendingReviews = get(),
            translations = get()
        )
    }

    viewModel {
        AdminHomeViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            filterController = get(),
            queryRestaurants = get(),
            queryUsers = get()
        )
    }

    viewModel {
        FilterViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            filterController = get()
        )
    }

    single<OwnerHomeTranslations> { OwnerHomeTranslationsImpl(androidContext().resources) }

    single<FilterController> { FilterControllerImpl() }
}
