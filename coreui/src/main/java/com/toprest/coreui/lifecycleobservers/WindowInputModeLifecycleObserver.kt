package com.toprest.coreui.lifecycleobservers

import android.view.Window
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class WindowInputModeLifecycleObserver(
    private val desiredMode: Int,
    private val windowGetter : () -> Window
): LifecycleObserver {

    private var originalMode = SOFT_INPUT_STATE_UNSPECIFIED

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun setNewSoftInputMode() {
        windowGetter().let {
            originalMode = it.attributes.softInputMode
            it.setSoftInputMode(desiredMode)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun revertSoftInputMode() {
        windowGetter().setSoftInputMode(originalMode)
    }
}
