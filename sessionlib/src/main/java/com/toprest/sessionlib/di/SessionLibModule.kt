package com.toprest.sessionlib.di

import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.sessionlib.client.SessionClient
import com.toprest.sessionlib.client.SessionClientImpl
import com.toprest.sessionlib.source.SessionSource
import com.toprest.sessionlib.source.SessionSourceImpl
import com.toprest.sessionlib.usecase.*
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun sessionLibModule(): Module = module {

    single<SessionClient> {
        SessionClientImpl(
            auth = get(),
            database = get(),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER))
        )
    }

    single<SessionSource> {
        SessionSourceImpl(get(), get())
    }

    single { Login(get()) }

    single { Logout(get()) }

    single { CreateUser(get()) }

    single { EditUser(get()) }

    single { QueryUser(get()) }

    single { QueryIsSignedIn(get()) }

    single { QueryUsers(get()) }

    single { QueryUserById(get()) }
}
