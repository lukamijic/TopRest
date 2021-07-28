package com.tempo.reply.di

import com.tempo.reply.ui.ReviewReplyViewModel
import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun reviewReplyModule() : Module = module {

    viewModel { (restaurantId: String, reviewId: String) ->
        ReviewReplyViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            restaurantId = restaurantId,
            reviewId = reviewId,
            replyToReview = get()
        )
    }
}
