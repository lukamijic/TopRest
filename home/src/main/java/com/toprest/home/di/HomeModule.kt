package com.toprest.home.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.home.ui.HomeViewModel
import com.toprest.home.ui.customer.CustomerHomeViewModel
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
            queryRestaurants = get()
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

    single<OwnerHomeTranslations> { OwnerHomeTranslationsImpl(androidContext().resources) }
}
