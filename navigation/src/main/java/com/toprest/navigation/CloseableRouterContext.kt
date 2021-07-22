package com.toprest.navigation

import java.util.*

interface CloseableRouterContext {
    val markedForClosing: LinkedList<String>
}
