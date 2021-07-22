package com.toprest.navigation

import java.util.*

class CloseableRouterContextImpl : CloseableRouterContext {
    override val markedForClosing = LinkedList<String>()
}
