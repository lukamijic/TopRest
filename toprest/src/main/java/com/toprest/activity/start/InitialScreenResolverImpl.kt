package com.toprest.activity.start

import com.toprest.navigation.RoutingActionsDispatcher

class InitialScreenResolverImpl(
    private val routingActionsDispatcher: RoutingActionsDispatcher
) : InitialScreenResolver {

    override fun showInitialScreen() {
        routingActionsDispatcher.dispatch { it.showLandingScreen() }
    }
}
