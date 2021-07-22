package com.toprest.core.util

/**
 * Object that holds a value T
 *
 *
 * Once the value is taken, Box is empty and it will return null until the new value is set
 */
data class Box<T>(private var value: T?) {

    companion object {

        fun <T> withInitialValue(value: T) = Box(value)

        fun <T> empty() = Box<T>(null)
    }

    fun set(value: T?): Box<T> {
        this.value = value
        return this
    }

    /**
     * Consumes value T if it is present. Box is guaranteed to be empty after a call to this method
     */
    fun take(): T? {
        val current = value
        value = null
        return current
    }

    fun hasValue() = value != null

    fun isEmpty() = value == null

    fun peak() = value

    fun put(value: T?) {
        this.value = value
    }

    inline fun <Result> fold(ifLeft: (T) -> Result, ifRight: () -> Result): Result =
        take()?.let(ifLeft) ?: ifRight()
}
