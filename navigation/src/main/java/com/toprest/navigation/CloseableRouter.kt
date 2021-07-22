package com.toprest.navigation

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.toprest.navigation.ext.peekBackStack

private const val ROUTING_ACTION_THROTTLE_WINDOW = 300

abstract class CloseableRouter(
    protected val fragmentManager: FragmentManager,
    private val closeableRouterContext: CloseableRouterContext
) :  Router {

    private val mainHandler = Handler(Looper.getMainLooper())

    private var lastActionStamp = 0L

    init {
        fragmentManager.addOnBackStackChangedListener {
            mainHandler.post {
                fragmentManager.peekBackStack()?.let {
                    if (!it.name.isNullOrBlank() && it.name == closeableRouterContext.markedForClosing.lastOrNull()) {
                        fragmentManager.popBackStackImmediate()
                        closeableRouterContext.markedForClosing.removeLast()
                    }
                } ?: removeAllMarkedForClosingEntries()
            }
        }
    }

    /** Dispatches action on main thread by posting the action. If the current thread is main thread, action is executed immediately. */
    protected fun dispatchOnMainThread(action: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action()
        } else {
            mainHandler.post(action::invoke)
        }
    }

    protected fun dispatchOnMainThreadWithThrottle(action: () -> Unit) {
        dispatchOnMainThread {
            System.currentTimeMillis().let {
                if (lastActionStamp < it - ROUTING_ACTION_THROTTLE_WINDOW) {
                    lastActionStamp = it
                    action()
                }
            }
        }
    }

    protected fun removeAllMarkedForClosingEntries() = dispatchOnMainThread { closeableRouterContext.markedForClosing.clear() }

    protected fun propagateBackToTopFragment(fragmentManager: FragmentManager): Boolean {
        return callIfPresent(findBackPropagatingFragment(fragmentManager), BackPropagatingFragment::back)
    }

    protected fun callIfPresent(backPropagatingFragment: BackPropagatingFragment?, function: (BackPropagatingFragment) -> Boolean): Boolean {
        return backPropagatingFragment?.let(function::invoke) ?: false
    }

    protected fun findBackPropagatingFragment(fragmentManager: FragmentManager): BackPropagatingFragment? =
        fragmentManager.fragments.reversed().firstOrNull { it is BackPropagatingFragment } as? BackPropagatingFragment
}
