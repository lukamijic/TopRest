package com.toprest.activity.start

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.toprest.R
import com.toprest.coreui.activity.BaseRoutingActivity
import org.koin.android.ext.android.inject

class StartActivity : BaseRoutingActivity() {

    companion object {

        fun createIntent(context: Context) = Intent(context, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    private val initialScreenResolver : InitialScreenResolver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initialScreenResolver.showInitialScreen()
    }
}
