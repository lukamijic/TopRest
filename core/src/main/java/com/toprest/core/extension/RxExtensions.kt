package com.toprest.core.extension

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.internal.functions.Functions
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.pow

private const val REF_COUNT_TIMEOUT_SECONDS = 1L

/** Shares the chain and replays the latest emission to new subscribers. Delays the chain dispose for [REF_COUNT_TIMEOUT_SECONDS] */
fun <T> Flowable<T>.shareReplayLatest(): Flowable<T> = replay(1).refCount(REF_COUNT_TIMEOUT_SECONDS, TimeUnit.SECONDS)

/** Subscribes an empty subscriber with an onError action */
inline fun Completable.subscribeWithOnError(crossinline onError: (Throwable) -> Unit): Disposable = subscribe({}) { onError(it) }

/** Logs an error retries indefinitely */
fun <T> Flowable<T>.logErrorAndRetry(): Flowable<T> = retry { throwable ->
    Timber.e(throwable)
    true
}

/** Subscribes an empty subscriber */
fun <T> Flowable<T>.subscribeEmpty(): Disposable = subscribe(Functions.emptyConsumer<Any>())

/**
 * Switch-maps to the source when [signal] emmits true.
 * Switch-maps to Flowable.never() while [signal] emmits false.
 */
fun <T> Flowable<T>.resubscribeWhen(signal: Flowable<Boolean>): Flowable<T> =
        signal.switchMap { if (it) this else Flowable.never() }

/**
 * Switch-maps to the source when [signal] emmits true.
 * Switch-maps to Completable.never() while [signal] emmits false.
 */
fun Completable.resubscribeWhen(signal: Flowable<Boolean>): Completable =
        signal.switchMapCompletable { if (it) this else Completable.never() }

/**
 * Switch-maps to the source when [condition] is true.
 * Switch-maps to Flowable.never() while [condition] is false.
 */
fun <T> Flowable<T>.resubscribeWhen(condition: (T) -> Boolean): Flowable<T> =
        switchMap { if (condition(it)) this else Flowable.never() }

/**
 * Creates flowables from the List<[Input]> and performs combine latest on them with smart cast to List<[Output]>.
 * Also handles the case when List<[Input]> is empty.
 */
inline fun <Input, reified Output> Flowable<List<Input>>.combineLatest(crossinline flowablesFactory: (List<Input>) -> List<Flowable<Output>>): Flowable<List<Output>> =
        switchMap { input ->
            if (input.isEmpty()) Flowable.just(emptyList())
            else Flowable.combineLatest(flowablesFactory(input)) { it.filterIsInstance<Output>() }
        }

/**
 * Creates flowables from the List<[Input]> and performs combine latest on them with smart cast to List<[Output]>.
 * Also handles the case when List<[Input]> is empty.
 */
inline fun <Input, reified Output> List<Input>.combineLatest(crossinline supplier: (Input) -> Flowable<Output>): Flowable<List<Output>> =
        if (this.isEmpty()) {
            Flowable.just(emptyList())
        } else {
            Flowable.combineLatest(this.map { supplier.invoke(it) }) { it.filterIsInstance<Output>() }
        }

/** Immediately emits the item and then debounces further emissions. */

fun <T> Flowable<T>.debounceImmediate(intervalDuration: Long, timeUnit: TimeUnit): Flowable<T> =
        shareReplayLatest().let { it.skipFirst().debounce(intervalDuration, timeUnit).startWith(it.takeOne()) }

/** Subscribes an empty subscriber with an onError action */
inline fun <T> Flowable<T>.subscribeWithOnError(crossinline onError: (Throwable) -> Unit): Disposable = subscribe({}) { onError(it) }

/** Skips the first item */
fun <T> Flowable<T>.skipFirst(): Flowable<T> = skip(1)

/** Takes the first item */
fun <T> Flowable<T>.takeOne(): Flowable<T> = take(1)

/** Delays the emission of item if the predicate returns true */
fun <T> Flowable<T>.delayIf(predicate: (T) -> Boolean, delay: Long, scheduler: Scheduler): Flowable<T> =
        switchMap { if (predicate(it)) Flowable.timer(delay, TimeUnit.MILLISECONDS, scheduler).map { _ -> it } else Flowable.just(it) }

/** Emits the given item */
fun <T> T.asFlowable(): Flowable<T> = Flowable.just(this)

fun <T> Flowable<T>.exponentialRetry(
    retryCount: Int = 6,
    initialDelayInMs: Long = 300,
    baseExponentialDelayInSeconds: Int = 2,
    scheduler: Scheduler
): Flowable<T> {
    data class ExponentialRetryInfo(val throwable: Throwable, val exponent: Int)

    fun Int.pow(x: Int) = toDouble().pow(x.toDouble()).toLong()

    return retryWhen { throwable ->
        Flowable.zip(
            throwable,
            Flowable.range(0, retryCount + 1), // +1 because we propagate the error on the last one
            BiFunction(::ExponentialRetryInfo)
        )
            .flatMapSingle { info ->
                when {
                    info.exponent == 0 -> Single.timer(initialDelayInMs, TimeUnit.MILLISECONDS).observeOn(
                        scheduler
                    )
                    info.exponent < retryCount -> Single.timer(
                        baseExponentialDelayInSeconds.pow(
                            info.exponent
                        ), TimeUnit.SECONDS
                    ).observeOn(scheduler)
                    else -> Single.error(info.throwable)
                }
            }
    }
}
