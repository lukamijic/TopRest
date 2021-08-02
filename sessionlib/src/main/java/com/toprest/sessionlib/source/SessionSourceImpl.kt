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

    private val users = client.queryUsers().map { it.map(ApiUser::toUser) }.shareReplayLatest()

    override fun isSignedIn(): Flowable<Boolean> = isSignedIn

    override fun user(): Flowable<User> = users.map {
        it.find { user -> firebaseAuth.currentUser?.let { currentUser -> currentUser.uid == user.id } ?: false } ?: User.EMPTY
    }
        .repeatWhen { isSignedIn }
        .shareReplayLatest()

    override fun login(email: String, password: String): Completable =
        client.login(email, password)
            .andThen(refreshSignedIn())

    override fun logOut(): Completable = client.logout()
        .andThen(refreshSignedIn())

    override fun editUser(userId: String, firstName: String, lastName: String, userType: UserType): Completable =
        client.editUser(userId, firstName, lastName, userType)

    override fun createUser(firstName: String, lastName: String, userType: UserType, email: String, password: String): Completable =
        client.createUser(email, password)
            .flatMapCompletable { (client.storeUserData(it, firstName, lastName, email, userType)) }
            .andThen(refreshSignedIn())

    override fun queryUsers(): Flowable<List<User>> = users

    fun refreshSignedIn() = Completable.fromAction { refreshSignedIn.onNext(REFRESH_SIGNED_IN) }
}
