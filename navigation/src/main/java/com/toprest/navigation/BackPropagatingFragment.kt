package com.toprest.navigation

interface BackPropagatingFragment {

    /** @return true if this fragment handled the back button, false to propagate it further down the chain */
    fun back(): Boolean
}
