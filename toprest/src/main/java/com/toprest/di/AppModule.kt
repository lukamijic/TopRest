package com.toprest.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.toprest.activity.start.InitialScreenResolver
import com.toprest.activity.start.InitialScreenResolverImpl
import com.toprest.navigation.Router
import com.toprest.navigation.RouterImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(): Module = module {

    factory<Router> {
        val activity: AppCompatActivity = it[0]
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        RouterImpl(activity, fragmentManager, get())
    }

    factory<InitialScreenResolver> { InitialScreenResolverImpl(get(), get()) }
}
