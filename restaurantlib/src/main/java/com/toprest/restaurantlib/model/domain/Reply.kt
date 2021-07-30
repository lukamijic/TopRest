package com.toprest.restaurantlib.model.domain

data class Reply(
    val reply: String,
    val creationTimeStamp: Long
) {

    companion object {
        val EMPTY = Reply("", -1)
    }

    fun isEmpty() = this === EMPTY
}
