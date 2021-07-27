package com.toprest.sessionlib.client

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.toprest.firebaselib.client.FirebaseClient
import com.toprest.sessionlib.model.api.ApiUser
import com.toprest.sessionlib.model.domain.UserType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

private const val USERS_NODE = "users"

class SessionClientImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    backgroundScheduler: Scheduler
) : SessionClient, FirebaseClient(backgroundScheduler) {

    override fun getUser(): Single<ApiUser> = auth.currentUser?.let {
        database.child(USERS_NODE).child(it.uid).get()
            .get<DataSnapshot>()
            .map { snapShot -> snapShot.getValue<ApiUser>() }
    } ?: Single.just(ApiUser.EMPTY)

    override fun login(email: String, password: String): Completable =
        auth.signInWithEmailAndPassword(email, password)
            .execute()

    override fun logout(): Completable = Completable.fromAction {
        auth.signOut()
    }

    override fun createUser(email: String, password: String): Single<String> =
        auth.createUserWithEmailAndPassword(email, password)
            .get<AuthResult>()
            .map { it.user!!.uid }


    override fun storeUserData(id: String, firstName: String, lastName: String, email: String, userType: UserType): Completable =
        database.child(USERS_NODE).child(auth.uid!!).setValue(
            ApiUser(id, firstName, lastName, email, userType.name)
        ).execute()
}
