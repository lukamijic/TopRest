package com.toprest.navigation

interface RoutingActionsSource {

    fun setActiveRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer)

    fun unsetRoutingActionConsumer(routingActionConsumer: RoutingActionConsumer)
}
