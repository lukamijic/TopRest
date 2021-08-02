package com.toprest.application

import android.app.Application
import com.tempo.reply.di.reviewReplyModule
import com.toprest.addrestaurant.di.addRestaurantModule
import com.toprest.appconfig.StethoAppConfig
import com.toprest.appconfig.TimberAppConfig
import com.toprest.core.di.threadingModule
import com.toprest.dashboard.di.dashboardModule
import com.toprest.di.appModule
import com.toprest.di.signUpModule
import com.toprest.editreply.di.editReplyModule
import com.toprest.editrestaurant.di.editRestaurantModule
import com.toprest.editreview.di.editReviewModule
import com.toprest.firebaselib.di.firebaseLibModule
import com.toprest.home.di.homeModule
import com.toprest.landing.di.landingModule
import com.toprest.login.di.loginModule
import com.toprest.navigation.navigationModule
import com.toprest.profile.di.profileModule
import com.toprest.restaurantlib.di.restaurantLibModule
import com.toprest.leavereview.di.leaveReviewModule
import com.toprest.restaurantdetails.di.restaurantDetailsModule
import com.toprest.sessionlib.di.sessionLibModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TopRestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()

        TimberAppConfig().configure()
        StethoAppConfig(this).configure()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TopRestApplication)
            modules(
                listOf(
                    addRestaurantModule(),
                    appModule(),
                    dashboardModule(),
                    editReplyModule(),
                    editRestaurantModule(),
                    editReviewModule(),
                    firebaseLibModule(),
                    homeModule(),
                    landingModule(),
                    leaveReviewModule(),
                    loginModule(),
                    navigationModule(),
                    profileModule(),
                    restaurantDetailsModule(),
                    restaurantLibModule(),
                    reviewReplyModule(),
                    sessionLibModule(),
                    signUpModule(),
                    threadingModule()
                )
            )
        }
    }
}
