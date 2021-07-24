package com.toprest.ui.registername

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseViewModel
import com.toprest.model.SignUpScreen
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction

class RegisterNameViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val controller: SignUpController
) : BaseViewModel<RegisterNameViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(controller.setScreen(SignUpScreen.REGISTER_NAME))

        query(
            Flowable.combineLatest(
                controller.firstName(),
                controller.lastName(),
                BiFunction(::RegisterNameViewState)
            ).take(1)
        )
    }

    fun setFirstName(firstName: String) = runCommand(controller.setFirstName(firstName))

    fun setLastName(lastName: String) = runCommand(controller.setLastName(lastName))
}
