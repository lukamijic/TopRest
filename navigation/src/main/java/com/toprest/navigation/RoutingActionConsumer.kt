package com.toprest.navigation

interface RoutingActionConsumer {

    fun onRoutingAction(routingAction: (Router) -> Unit)
}
