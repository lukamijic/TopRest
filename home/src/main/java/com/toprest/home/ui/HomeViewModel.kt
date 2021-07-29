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
    queryUser: QueryUser
) : BaseViewModel<HomeViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(
            queryUser()
                .map(User::userType)
                .distinctUntilChanged()
                .toCompletable { userType : UserType ->
                    when (userType) {
                        UserType.CUSTOMER -> dispatchRoutingAction(Router::showCustomerHome)
                        UserType.OWNER -> dispatchRoutingAction(Router::showCustomerHome)
                        UserType.ADMIN -> dispatchRoutingAction(Router::showCustomerHome)
                    }
                }
        )
    }
}
