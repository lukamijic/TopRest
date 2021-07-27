package com.toprest.addrestaurant.ui

import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.usecase.CreateRestaurant
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

class AddRestaurantViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val queryUser: QueryUser,
    private val createRestaurant: CreateRestaurant
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
            combineLatest(
                queryUser().map(User::id),
                restaurantName,
                restaurantDescription,
                Function3(CreateRestaurant::Param)
            )
                .firstOrError()
                .flatMapCompletable(createRestaurant::invoke)
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeAddRestaurant)
}
