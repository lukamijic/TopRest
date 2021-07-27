package com.toprest.leavereview.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.leavereview.ui.LeaveReviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun leaveReviewModule() : Module = module {

    viewModel { (restaurantId : String ) ->
        LeaveReviewViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            restaurantId,
            queryUser = get(),
            leaveReview = get()
        )
    }
}
