package com.toprest.coreui

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.toprest.core.extension.exponentialRetry
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.processors.BehaviorProcessor
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseViewModel<BaseViewState : Any>(
    protected val mainThreadScheduler: Scheduler,
    protected val backgroundScheduler: Scheduler,
    private val routingActionsDispatcher: RoutingActionsDispatcher
) : ViewModel() {

    private val typeToPublisher: MutableMap<KClass<BaseViewState>, BehaviorProcessor<BaseViewState>> =
        mutableMapOf()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun viewStates(): Collection<Flowable<BaseViewState>> = typeToPublisher.values

    protected inline fun <reified T : BaseViewState> query(flowable: Flowable<T>) =
        queryInternal(T::class as KClass<BaseViewState>, flowable as Flowable<BaseViewState>)

    protected fun <T> runCommand(single: Single<T>) = runCommand(single.ignoreElement())

    protected fun runCommand(completable: Completable) =
        addDisposable(
            completable
                .subscribeOn(backgroundScheduler)
                .subscribe(Functions.EMPTY_ACTION, Consumer(Timber::w))
        )

    /** Invoke to route to another screen */
    protected fun dispatchRoutingAction(routingAction: (Router) -> Unit) =
        routingActionsDispatcher.dispatch(routingAction)

    /** Invoke to route to another screen. If another routing action with the same [actionId] is already queued, the old one will be removed. */
    protected fun dispatchDistinctRoutingAction(actionId: String, routingAction: (Router) -> Unit) =
        routingActionsDispatcher.dispatchDistinct(actionId, routingAction)

    @Deprecated("This is for internal usage only! Protected because of inline function")
    protected fun queryInternal(key: KClass<BaseViewState>, flowable: Flowable<BaseViewState>) {
        if (typeToPublisher.containsKey(key)) throw IllegalArgumentException("Flowable<${key.simpleName}> already registered")

        val viewStatePublisher = BehaviorProcessor.create<BaseViewState>()
        typeToPublisher[key] = viewStatePublisher

        addDisposable(
            flowable
                .onBackpressureLatest()
                .observeOn(mainThreadScheduler)
                .subscribeOn(backgroundScheduler)
                .exponentialRetry(scheduler = backgroundScheduler)
                .subscribe(viewStatePublisher::onNext, Timber::e)
        )
    }

    @CallSuper
    override fun onCleared() {
        disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}
