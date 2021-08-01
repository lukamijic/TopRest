package com.toprest.home.ui.filter.controller

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface FilterController {

    fun minimumScore() : Flowable<Int>

    fun setMinimumScore(score: Int) : Completable
}
