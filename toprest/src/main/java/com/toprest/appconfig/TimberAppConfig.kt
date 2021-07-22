package com.toprest.appconfig

import com.toprest.core.appconfig.AppConfig
import timber.log.Timber

class TimberAppConfig : AppConfig {

    override fun configure() {
        Timber.plant(Timber.DebugTree())
    }
}
