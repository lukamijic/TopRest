package com.toprest.core.usecase

import io.reactivex.rxjava3.core.Flowable

interface QueryUseCaseWithParam<Param, Result> {

    operator fun invoke(param: Param): Flowable<Result>
}
