package com.toprest.core.usecase

import com.toprest.core.extension.shareReplayLatest
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer

/**
 * Caches created query by [Param] which has to override equals and hashcode.
 **/
abstract class QueryUseCaseWithCachedParam<Param, Result> : QueryUseCaseWithParam<Param, Result> {

    private val flowableCache: MutableMap<Param, Flowable<Result>> = HashMap()

    final override fun invoke(param: Param): Flowable<Result> = getOrCreateFlowable(param)

    protected abstract fun createQuery(param: Param): Flowable<Result>

    @Synchronized
    private fun getOrCreateFlowable(param: Param): Flowable<Result> {
        flowableCache[param]?.let { return it }
        val flowable = createQuery(param)
            .doFinally { removeFromCache(param) }
            .compose(cachingStrategy())
        flowableCache[param] = flowable
        return flowable
    }

    @Synchronized
    private fun removeFromCache(key: Param) = flowableCache.remove(key)

    protected open fun cachingStrategy(): FlowableTransformer<Result, Result> = FlowableTransformer { it.shareReplayLatest() }
}
