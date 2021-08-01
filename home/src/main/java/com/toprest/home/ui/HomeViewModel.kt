package com.toprest.home.ui

import com.toprest.core.extension.toCompletable
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.sessionlib.usecase.QueryUser
import io.reactivex.rxjava3.core.Scheduler

class HomeViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val queryUser: QueryUser
) : BaseViewModel<HomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    fun showUserBasedHomeScreen() = runCommand(
        queryUser()
            .map(User::userType)
            .firstOrError()
            .toCompletable { userType : UserType ->
                when (userType) {
                    UserType.CUSTOMER -> dispatchRoutingAction(Router::showCustomerHome)
                    UserType.OWNER -> dispatchRoutingAction(Router::showOwnerHome)
                    UserType.ADMIN -> dispatchRoutingAction(Router::showCustomerHome)
                }
            }
    )
}
