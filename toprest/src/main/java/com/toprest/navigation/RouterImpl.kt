package com.toprest.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.toprest.R
import com.toprest.landing.ui.LandingFragment
import com.toprest.navigation.ext.applyShortFadeInEnterAndFadeOutExitAnimation
import com.toprest.navigation.ext.inTransaction
import com.toprest.navigation.ext.inTransactionAndAddToBackStack
import com.toprest.navigation.ext.safeClearBackStack

private const val LAST_FRAGMENT = 0

private const val START_CONTAINER = R.id.start_container

class RouterImpl(
    private val activity: AppCompatActivity,
    fragmentManager: FragmentManager,
    closeableRouterContext: CloseableRouterContext
) : CloseableRouter(fragmentManager, closeableRouterContext), Router {

    override fun finishHostActivity() {
        activity.finish()
    }

    override fun clearAll() = fragmentManager.safeClearBackStack()

    override fun goBack() = dispatchOnMainThreadWithThrottle(this::goBackInternal)

    private fun goBackInternal() {
        if (!propagateBackToTopFragment(fragmentManager)) {
            if (fragmentManager.backStackEntryCount != LAST_FRAGMENT) {
                fragmentManager.popBackStackImmediate()
            } else {
                finishHostActivity()
            }
        }
    }

    override fun showLandingScreen() {
        fragmentManager.inTransaction {
            add(START_CONTAINER, LandingFragment.newInstance(), LandingFragment.TAG)
        }
    }
}
