package com.toprest.firebaselib.client

import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

abstract class FirebaseClient(
    private val backgroundScheduler: Scheduler
) {

    fun <T> Task<T>.execute() = Single.create<T> { emitter ->
        onCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(it.result!!)
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }

    fun <T> Task<T>.execute(onSuccess: () -> Unit) = Completable.create { emitter ->
        onCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
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
