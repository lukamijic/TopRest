package com.toprest.core.usecase

import io.reactivex.rxjava3.core.Completable

interface CommandUseCase {

    operator fun invoke(): Completable
}
