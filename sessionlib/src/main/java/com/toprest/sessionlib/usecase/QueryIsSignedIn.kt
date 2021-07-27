package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.QueryUseCase
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Flowable

class QueryIsSignedIn(private val sessionSource: SessionSource) : QueryUseCase<Boolean> {

    override fun invoke(): Flowable<Boolean> = sessionSource.isSignedIn()
}
