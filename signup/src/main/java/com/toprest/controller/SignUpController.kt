package com.toprest.controller

import com.toprest.sessionlib.model.domain.UserType
import com.toprest.model.SignUpScreen
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface SignUpController {

    fun setScreen(signUpScreen: SignUpScreen): Completable

    fun title(): Flowable<String>

    fun actionButtonEnabled(): Flowable<Boolean>

    fun actionButtonText(): Flowable<String>

    fun actionButtonClicked(): Completable

    fun previous(): Completable

    fun loading() : Flowable<Boolean>

    fun firstName(): Flowable<String>

    fun lastName(): Flowable<String>

    fun userType(): Flowable<UserType>

    fun email(): Flowable<String>

    fun isEmailValid(): Flowable<Boolean>

    fun password(): Flowable<String>

    fun repeatedPassword(): Flowable<String>

    fun isPasswordValid(): Flowable<Boolean>

    fun arePasswordsMatched(): Flowable<Boolean>

    fun accountCreationFailedEvents(): Flowable<Any>

    fun setLoading(isLoading: Boolean) : Completable

    fun setFirstName(firstName: String): Completable

    fun setLastName(lastName: String): Completable

    fun setUserType(userType: UserType): Completable

    fun setEmail(email: String): Completable

    fun setPassword(password: String) : Completable

    fun setRepeatedPassword(repeatedPassword: String) : Completable
}
