package com.toprest.firebaselib.client

import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

abstract class FirebaseClient(
    private val backgroundScheduler: Scheduler
) {

    fun <T, U> Task<T>.get(mapper: (T?) -> U) = Single.create<U> {emitter ->
        onCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(mapper(it.result))
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }

    fun <T> Task<T>.get() = Single.create<T> { emitter ->
        onCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(it.result)
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }

    fun <T> Task<T>.execute() = Completable.create { emitter ->
        onCompleteListener {
            if (it.isSuccessful) {
                emitter.onComplete()
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }

    private fun <T> Task<T>.onCompleteListener(action: (Task<T>) -> Unit) {
        addOnCompleteListener {
            backgroundScheduler.scheduleDirect { action(it) }
        }
    }
}
