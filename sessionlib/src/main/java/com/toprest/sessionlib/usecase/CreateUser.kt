package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Completable

class CreateUser(private val sessionSource: SessionSource) : CommandUseCaseWithParam<CreateUser.Param> {

    override fun invoke(param: CreateUser.Param): Completable =
        sessionSource.createUser(param.firstName, param.lastName, param.userType, param.email, param.password)

    data class Param(
        val firstName: String,
        val lastName: String,
        val userType: UserType,
        val email: String,
        val password: String
    )
}
