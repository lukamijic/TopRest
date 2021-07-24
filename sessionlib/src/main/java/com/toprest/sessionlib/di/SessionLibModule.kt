package com.toprest.sessionlib.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.sessionlib.source.SessionSource
import com.toprest.sessionlib.source.SessionSourceImpl
import com.toprest.sessionlib.usecase.CreateUser
import com.toprest.sessionlib.usecase.Login
import com.toprest.sessionlib.usecase.Logout
import com.toprest.sessionlib.usecase.QueryUser
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun sessionLibModule(): Module = module {

    single<SessionSource> {
        SessionSourceImpl(
            auth = get(),
            database = get(),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
        )
    }

    single { Login(get()) }

    single { Logout(get()) }

    single { CreateUser(get()) }

    single { QueryUser(get()) }
}
