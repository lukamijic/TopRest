package com.toprest.activity.start

import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.usecase.QueryIsSignedIn

class InitialScreenResolverImpl(
    private val routingActionsDispatcher: RoutingActionsDispatcher,
    private val queryIsSignedIn: QueryIsSignedIn
) : InitialScreenResolver {

    override fun showInitialScreen() {
        queryIsSignedIn()
            .firstOrError()
            .subscribe { isSignedIn ->
                if (isSignedIn) {
                    routingActionsDispatcher.dispatch { it.showMainActivity() }
                } else {
                    routingActionsDispatcher.dispatch { it.showLandingScreen() }
                }
            }
    }
}
