package com.toprest.activity.main

import android.content.Context
import android.content.Intent
import com.toprest.coreui.activity.BaseActivity
import com.toprest.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<Unit, ActivityMainBinding>(ActivityMainBinding::inflate) {

    companion object {

        fun createIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    override val model: MainViewModel by viewModel()
}
