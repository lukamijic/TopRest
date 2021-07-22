package com.toprest.core.robot

/**
 * Robot which runs only when the application is active initializing flow when the MainActivity is started and cleaning up code when the application is stopped
 */
interface Robot {

    /**
     * Called as part of onStart for MainActivity
     */
    fun bootUp(contextHash: Int)

    /**
     * Called as part of onStop for MainActivity
     */
    fun initiateSelfDestruction(contextHash: Int)
}
