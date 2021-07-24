package com.toprest.core.usecase

import io.reactivex.rxjava3.core.Flowable

interface QueryUseCase<Result> {

    operator fun invoke(): Flowable<Result>
}
