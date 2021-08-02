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
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

private const val USERS_NODE = "users"
private const val USER_FIRST_NAME_NODE = "firstName"
private const val USER_LAST_NAME_NODE = "lastName"
private const val USER_USER_TYPE_NODE = "userType"

class SessionClientImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    backgroundScheduler: Scheduler
) : SessionClient, FirebaseClient(backgroundScheduler) {

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

    override fun editUser(userId: String, firstName: String, lastName: String, userType: UserType): Completable {
        val userNode = database.child(USERS_NODE).child(userId)
        val updateFields = mapOf(
            USER_FIRST_NAME_NODE to firstName,
            USER_LAST_NAME_NODE to lastName,
            USER_USER_TYPE_NODE to userType.name
        )

        return userNode.updateChildren(updateFields).execute()
    }

    override fun queryUsers(): Flowable<List<ApiUser>> =
        database
            .child(USERS_NODE)
            .query { dataSnapshot ->
                dataSnapshot.children.map { userSnapshot -> userSnapshot.getValue<ApiUser>()!! }
            }
}
