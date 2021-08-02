package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.QueryUseCaseWithParam
import com.toprest.sessionlib.model.domain.User
import io.reactivex.rxjava3.core.Flowable

class QueryUserById(private val queryUsers: QueryUsers) : QueryUseCaseWithParam<String, User> {

    override fun invoke(param: String): Flowable<User> = queryUsers().map { it.find { user -> user.id == param } ?: User.EMPTY }
}
