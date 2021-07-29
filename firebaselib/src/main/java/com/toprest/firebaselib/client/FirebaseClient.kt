package com.toprest.firebaselib.client

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.*

abstract class FirebaseClient(
    private val backgroundScheduler: Scheduler
) {

    fun <T> DatabaseReference.query(mapper: (DataSnapshot) -> T) = Flowable.create<T>(
        { emitter ->
            onValueChanged({ dataSnapshot -> emitter.onNext(mapper(dataSnapshot)) }) {
                emitter.onError(it.toException())
            }
        }, BackpressureStrategy.BUFFER
    )

    fun <T, U> Task<T>.get(mapper: (T?) -> U) = Single.create<U> { emitter ->
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

    private fun DatabaseReference.onValueChanged(valueChangedAction: (DataSnapshot) -> Unit, onCancelled : (DatabaseError) -> Unit) {
        addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                backgroundScheduler.scheduleDirect { valueChangedAction(snapshot) }
            }

            override fun onCancelled(error: DatabaseError) {
                backgroundScheduler.scheduleDirect { onCancelled(error) }
            }
        })
    }
}
