package com.toprest.addrestaurant.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

class AddRestaurantViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<AddRestaurantViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val restaurantName = BehaviorProcessor.createDefault("")
    private val restaurantDescription = BehaviorProcessor.createDefault("")

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(
            combineLatest(
                restaurantName,
                restaurantDescription,
                { title, desc -> AddRestaurantViewState.Button(title.isNotBlank() && desc.isNotBlank()) }
            ).observeOn(backgroundScheduler)
        )

        query(
            isLoading
                .observeOn(backgroundScheduler)
                .map(AddRestaurantViewState::Loading)
        )
    }


    fun setRestaurantTitle(title: String) = restaurantName.onNext(title)

    fun setRestaurantDescription(description: String) = restaurantDescription.onNext(description)

    fun addRestaurant() {
        isLoading.onNext(true)
        runCommand(
            Completable.fromAction {  }
                .delay(3000, TimeUnit.MILLISECONDS)
                .doOnComplete { close() }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeAddRestaurant)
}
