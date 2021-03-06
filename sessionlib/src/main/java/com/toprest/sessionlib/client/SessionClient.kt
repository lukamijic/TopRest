package com.toprest.sessionlib.client

import com.toprest.sessionlib.model.api.ApiUser
import com.toprest.sessionlib.model.domain.UserType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface SessionClient {

    fun login(email: String, password: String) : Completable

    fun logout() : Completable

    fun createUser(email: String, password: String) : Single<String>

    fun storeUserData(id: String, firstName: String, lastName: String, email: String, userType: UserType) : Completable

    fun editUser(userId: String, firstName: String, lastName: String, userType: UserType) : Completable

    fun queryUsers(): Flowable<List<ApiUser>>
}
