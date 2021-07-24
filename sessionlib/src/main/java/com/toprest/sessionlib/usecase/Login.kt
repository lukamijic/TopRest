package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Completable

class Login(private val sessionSource: SessionSource) : CommandUseCaseWithParam<Login.Param> {

    override fun invoke(param: Param): Completable = sessionSource.login(param.email, param.password)

    data class Param(val email: String, val password: String)
}
