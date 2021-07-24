package com.toprest.coreui.utils

import android.view.View

private const val THROTTLE_DURATION = 400
private var lastClickTimestamp: Long = 0

fun View.onThrottledClick(action: (View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTimestamp > THROTTLE_DURATION) {
            lastClickTimestamp = currentTime
            action(it)
        }
    }
}

fun View.onThrottledClick(listener: View.OnClickListener?) {
    onThrottledClick { listener?.onClick(it) }
}
