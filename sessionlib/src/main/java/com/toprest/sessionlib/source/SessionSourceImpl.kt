package com.toprest.sessionlib.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.toprest.sessionlib.model.api.ApiUser
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.processors.PublishProcessor

private const val USERS_NODE = "users"

private val REFRESH_USER_EVENT = Any()

class SessionSourceImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val backgroundScheduler: Scheduler
) : SessionSource {

    private val refreshUserEvent = PublishProcessor.create<Any>()

    override fun user(): Flowable<User> = Flowable.create<User>(
        { emitter ->
            auth.currentUser?.let {
                database.child(USERS_NODE).child(it.uid).get()
                    .addOnSuccessListener { result ->
                        emitter.onNext(result.getValue<ApiUser>()!!.toUser())
                        emitter.onComplete()
                    }
                    .addOnFailureListener { throwable -> emitter.onError(throwable) }
            } ?: emitter.onNext(User.EMPTY)
        },
        BackpressureStrategy.BUFFER
    ).observeOn(backgroundScheduler)
        .repeatWhen { refreshUserEvent }

    override fun login(email: String, password: String): Completable =
        Completable.create { emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        refreshUserEvent.onNext(REFRESH_USER_EVENT)
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
        }.observeOn(backgroundScheduler)

    override fun logOut(): Completable = Completable.fromAction {
        auth.signOut()
        refreshUserEvent.onNext(REFRESH_USER_EVENT)
    }

    override fun createUser(firstName: String, lastName: String, userType: UserType, email: String, password: String): Completable =
        Single.create<String> { emitter ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(auth.currentUser!!.uid)
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
        }
            .observeOn(backgroundScheduler)
            .flatMapCompletable { storeUserData(ApiUser(it, firstName, lastName, email, userType.name)) }

    private fun storeUserData(apiUser: ApiUser) = Completable.create { emitter ->
        database.child(USERS_NODE).child(apiUser.id!!).setValue(apiUser)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    refreshUserEvent.onNext(REFRESH_USER_EVENT)
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception!!)
                }
            }
    }.observeOn(backgroundScheduler)
}
