package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.QueryUseCase
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Flowable

class QueryUsers(private val sessionSource: SessionSource) : QueryUseCase<List<User>> {

    override fun invoke(): Flowable<List<User>> = sessionSource.queryUsers()
}
