package com.toprest.sessionlib.source

import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface SessionSource {

    fun user(): Flowable<User>

    fun login(email: String, password: String) : Completable

    fun createUser(firstName: String, lastName: String, userType: UserType, email: String, password: String) : Completable

    fun logOut() : Completable
}
