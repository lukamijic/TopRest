package com.toprest.editrestaurant.ui

import com.toprest.core.extension.shareReplayLatest
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.restaurantlib.model.domain.Restaurant
import com.toprest.restaurantlib.usecase.CreateRestaurant
import com.toprest.restaurantlib.usecase.DeleteRestaurant
import com.toprest.restaurantlib.usecase.EditRestaurant
import com.toprest.restaurantlib.usecase.QueryRestaurant
import com.toprest.sessionlib.model.domain.User
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class EditRestaurantViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryRestaurant: QueryRestaurant,
    private val editRestaurant: EditRestaurant,
    private val deleteRestaurant: DeleteRestaurant,
    private val restaurantId: String
) : BaseViewModel<EditRestaurantViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val restaurant = queryRestaurant(restaurantId).shareReplayLatest()

    private val restaurantName = BehaviorProcessor.createDefault("")
    private val restaurantDescription = BehaviorProcessor.createDefault("")

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(
            combineLatest(
                restaurant.observeOn(backgroundScheduler),
                restaurantName.observeOn(backgroundScheduler),
                restaurantDescription,
                ::isButtonEnabled
            ).map(EditRestaurantViewState::Button)
        )

        query(isLoading.observeOn(backgroundScheduler).map(EditRestaurantViewState::Loading))

        query(restaurant.map { EditRestaurantViewState.InitialData(it.name, it.description) }.take(1))
    }

    fun delete() {
        isLoading.onNext(true)
        runCommand(
            deleteRestaurant(restaurantId)
                .doOnComplete {
                    dispatchRoutingAction { it.closeRestaurantDetails() }
                    close()
                }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun edit() {
        isLoading.onNext(true)
        runCommand(
            combineLatest(
                restaurantName,
                restaurantDescription,
                { name, description -> EditRestaurant.Param(restaurantId, name, description) }
            )
                .firstOrError()
                .flatMapCompletable(editRestaurant::invoke)
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeEditRestaurant)

    fun setRestaurantTitle(title: String) = restaurantName.onNext(title)

    fun setRestaurantDescription(description: String) = restaurantDescription.onNext(description)

    private fun isButtonEnabled(restaurant: Restaurant, restaurantName: String, restaurantDescription: String) =
        if (restaurantName.isBlank() || restaurantDescription.isBlank()) {
            false
        } else {
            restaurant.name != restaurantName || restaurant.description != restaurantDescription
        }
}
