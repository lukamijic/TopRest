package com.toprest.edituser.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.edituser.ui.EditUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun editUserModule() : Module = module {

    viewModel { (userId: String) ->
        EditUserViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUserById = get(),
            editUser = get(),
            userId = userId
        )
    }
}
