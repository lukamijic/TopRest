package com.toprest.core.robot

import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseRobot : Robot {

    private var contextHash: Int? = null

    private var compositeDisposable = CompositeDisposable()

    final override fun bootUp(contextHash: Int) {
        this.contextHash = contextHash
        compositeDisposable.clear()
        onBootUp()
    }

    protected open fun onBootUp() {}

    final override fun initiateSelfDestruction(contextHash: Int) {
        if (this.contextHash == contextHash) {
            onDestroy()
            compositeDisposable.clear()
        }
    }

    protected open fun onDestroy() {}
}
