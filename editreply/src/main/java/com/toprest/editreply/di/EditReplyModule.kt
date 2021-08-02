package com.toprest.editreply.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.editreply.ui.EditReplyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun editReplyModule() : Module = module {

    viewModel { (restaurantId: String, reviewId: String) ->
        EditReplyViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            editReply = get(),
            deleteReply = get(),
            queryReply = get(),
            restaurantId = restaurantId,
            reviewId = reviewId
        )
    }
}
