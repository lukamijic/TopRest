package com.toprest.sessionlib.usecase

import com.toprest.core.usecase.CommandUseCase
import com.toprest.sessionlib.source.SessionSource
import io.reactivex.rxjava3.core.Completable

class Logout(private val sessionSource: SessionSource) : CommandUseCase {

    override fun invoke(): Completable = sessionSource.logOut()
}
