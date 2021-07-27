package com.toprest.profile.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.profile.ui.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun profileModule() : Module = module {

    viewModel {
        ProfileViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            logout = get()
        )
    }
}
