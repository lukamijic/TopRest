package com.toprest.sessionlib.source

import com.google.firebase.auth.FirebaseAuth
import com.toprest.core.extension.shareReplayLatest
import com.toprest.sessionlib.client.SessionClient
import com.toprest.sessionlib.model.api.ApiUser
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.processors.PublishProcessor

private val REFRESH_SIGNED_IN = Any()

class SessionSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val client: SessionClient
) : SessionSource {

    private val refreshSignedIn = PublishProcessor.create<Any>()

    private val isSignedIn = Single.fromCallable { firebaseAuth.currentUser?.let { true } ?: false }
        .repeatWhen { refreshSignedIn }
        .shareReplayLatest()

    override fun isSignedIn(): Flowable<Boolean> = isSignedIn

    override fun user(): Flowable<User> = client.getUser()
        .map(ApiUser::toUser)
        .repeatWhen { isSignedIn }
        .shareReplayLatest()

    override fun login(email: String, password: String): Completable =
        client.login(email, password)
            .andThen(refreshSignedIn())

    override fun logOut(): Completable = client.logout()
        .andThen(refreshSignedIn())

    override fun createUser(firstName: String, lastName: String, userType: UserType, email: String, password: String): Completable =
        client.createUser(email, password)
            .flatMapCompletable { (client.storeUserData(it, firstName, lastName, email, userType)) }
            .andThen(refreshSignedIn())

    private fun refreshSignedIn() = Completable.fromAction { refreshSignedIn.onNext(REFRESH_SIGNED_IN) }
}
