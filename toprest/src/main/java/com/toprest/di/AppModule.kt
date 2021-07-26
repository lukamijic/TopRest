package com.toprest.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.toprest.activity.main.MainViewModel
import com.toprest.activity.start.InitialScreenResolver
import com.toprest.activity.start.InitialScreenResolverImpl
import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.navigation.Router
import com.toprest.navigation.RouterImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun appModule(): Module = module {

    factory<Router> {
        val activity: AppCompatActivity = it[0]
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        RouterImpl(activity, fragmentManager, get())
    }

    factory<InitialScreenResolver> { InitialScreenResolverImpl(get(), get()) }

    viewModel {
        MainViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            queryUser = get()
        )
    }
}
