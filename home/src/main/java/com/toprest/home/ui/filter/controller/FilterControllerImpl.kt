package com.toprest.home.ui.filter.controller

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

class FilterControllerImpl : FilterController {

    private val minimumScore = BehaviorProcessor.createDefault(0)

    override fun minimumScore(): Flowable<Int> = minimumScore

    override fun setMinimumScore(score: Int): Completable = Completable.fromAction {
        minimumScore.onNext(score)
    }
}
