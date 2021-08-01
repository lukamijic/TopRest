package com.toprest.appconfig

import android.app.Application
import com.facebook.stetho.Stetho
import com.toprest.core.appconfig.AppConfig

class StethoAppConfig(private val application: Application) : AppConfig {

    override fun configure() {
        Stetho.initializeWithDefaults(application)
    }
}
