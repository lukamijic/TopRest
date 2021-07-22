package com.toprest.activity.start

import android.os.Bundle
import com.toprest.R
import com.toprest.coreui.activity.BaseRoutingActivity
import org.koin.android.ext.android.inject

class StartActivity : BaseRoutingActivity() {

    private val initialScreenResolver : InitialScreenResolver by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initialScreenResolver.showInitialScreen()
    }
}
