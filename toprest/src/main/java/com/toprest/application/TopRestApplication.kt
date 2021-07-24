package com.toprest.application

import android.app.Application
import com.toprest.appconfig.TimberAppConfig
import com.toprest.core.di.threadingModule
import com.toprest.di.appModule
import com.toprest.di.signUpModule
import com.toprest.landing.di.landingModule
import com.toprest.login.di.loginModule
import com.toprest.navigation.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TopRestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()

        TimberAppConfig().configure()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TopRestApplication)
            modules(
                listOf(
                    appModule(),
                    landingModule(),
                    loginModule(),
                    navigationModule(),
                    signUpModule(),
                    threadingModule()
                )
            )
        }
    }
}
