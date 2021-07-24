package com.toprest.core.usecase

import io.reactivex.rxjava3.core.Completable

interface CommandUseCaseWithParam<Param> {

    operator fun invoke(param: Param): Completable
}
