package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.CommandUseCaseWithParam
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Completable

class EditUser(private val sessionSource: SessionSource) : CommandUseCaseWithParam<EditUser.Param> {

    override fun invoke(param: Param): Completable =
        sessionSource.editUser(param.userId, param.firstName, param.lastName, param.userType)

    data class Param(val userId: String, val firstName: String, val lastName: String, val userType: UserType)
}
