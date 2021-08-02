package com.toprest.restaurantlib.model.domain

data class Review(
    val id: String,
    val restaurantId: String,
    val reviewerId: String,
    val review: String,
    val score: Int,
    val dateOfVisit: String,
    val creationTimeStamp: Long,
    val reply: Reply
) {

    companion object {
        val EMPTY = Review("", "", "", "", 0, "", -1L, Reply.EMPTY)
    }

    fun isEmpty() = this === EMPTY
}
