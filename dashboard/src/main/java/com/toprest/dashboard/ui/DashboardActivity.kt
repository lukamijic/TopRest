package com.toprest.dashboard.ui

import android.content.Context
import android.content.Intent
import com.toprest.coreui.activity.BaseActivity
import com.toprest.dashboard.databinding.ActivityDashboardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<Unit, ActivityDashboardBinding>(ActivityDashboardBinding::inflate) {

    companion object {

        fun createIntent(context: Context) = Intent(context, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }

    override val model: DashboardViewModel by viewModel()
}
