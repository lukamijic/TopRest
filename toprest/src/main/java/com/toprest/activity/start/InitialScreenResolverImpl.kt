package com.toprest.activity.start

import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.usecase.QueryUser

class InitialScreenResolverImpl(
    private val routingActionsDispatcher: RoutingActionsDispatcher,
    private val queryUser: QueryUser
) : InitialScreenResolver {

    override fun showInitialScreen() {
        queryUser()
            .firstOrError()
            .subscribe { user ->
                if (user == User.EMPTY) {
                    routingActionsDispatcher.dispatch { it.showLandingScreen() }
                } else {
                    routingActionsDispatcher.dispatch { it.showHome() }
                }
            }
    }
}
