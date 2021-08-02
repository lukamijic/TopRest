package com.toprest.editreview.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.editreview.ui.EditReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun editReviewModule(): Module = module {

    viewModel { (restaurantId: String, reviewId: String) ->
        EditReviewViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            editReview = get(),
            deleteReview = get(),
            queryReview = get(),
            restaurantId = restaurantId,
            reviewId = reviewId
        )
    }
}
