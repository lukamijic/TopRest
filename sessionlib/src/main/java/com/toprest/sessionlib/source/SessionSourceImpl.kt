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

    private val isSignedIn = Flowable.fromCallable { firebaseAuth.currentUser?.let { true } ?: false }
        .repeatWhen { refreshSignedIn }
        .shareReplayLatest()

    private val user = client.getUser()
        .map(ApiUser::toUser)
        .repeatWhen { isSignedIn }
        .shareReplayLatest()

    override fun isSignedIn(): Flowable<Boolean> = isSignedIn

    override fun user(): Flowable<User> = user

    override fun login(email: String, password: String): Completable =
        client.login(email, password)
            .doOnComplete { refreshSignedIn.onNext(REFRESH_SIGNED_IN) }

    override fun logOut(): Completable = client.logout()
        .doOnComplete { refreshSignedIn.onNext(REFRESH_SIGNED_IN) }

    override fun createUser(firstName: String, lastName: String, userType: UserType, email: String, password: String): Completable =
        client.createUser(email, password)
            .flatMapCompletable { (client.storeUserData(it, firstName, lastName, email, userType)) }
            .doOnComplete { refreshSignedIn.onNext(REFRESH_SIGNED_IN) }
}
