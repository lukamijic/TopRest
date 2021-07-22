package com.toprest.core.di

import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val SERIAL_BACKGROUND_SCHEDULER = "SERIAL_BACKGROUND_SCHEDULER"
const val BACKGROUND_SCHEDULER = "BACKGROUND_SCHEDULER"
const val MAIN_SCHEDULER = "MAIN_SCHEDULER"

fun threadingModule(): Module = module {

    single(named(SERIAL_BACKGROUND_SCHEDULER)) { Schedulers.single() }

    single(named(BACKGROUND_SCHEDULER)) { Schedulers.io() }

    single<Scheduler>(named(MAIN_SCHEDULER)) { StrictMainScheduler() }
}

private class StrictMainScheduler : Scheduler() {

    private val mainScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)

    override fun createWorker() = object : Worker() {

        private val worker = mainScheduler.createWorker()

        override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                /* We need to schedule throwing the exception to avoid the issue where Replay will catch the exception and swallow it.
                 By scheduling it, we're moving it to the end of the work queue, ensuring that Replay logic is fully executed.
                 However, if we schedule it, we're losing the valuable information from the stack trace.
                 Therefore, we're throwing the exception and immediately catching it in order to preserve the stack trace.
                 After that, we're scheduling rethrowing that same exception, to avoid the Replay issue.

                 Additionally, we're using Timber.e to crash the app in the debug version and log the error in the release version.
                */
                try {
                    Timber.e(IllegalStateException("RxChain already on MainThread!"))
                } catch (exception: IllegalStateException) {
                    worker.schedule { throw exception }
                }
            }

            return worker.schedule(run, delay, unit)
        }

        override fun dispose() = worker.dispose()

        override fun isDisposed() = worker.isDisposed
    }
}
