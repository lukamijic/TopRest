package com.toprest.restaurantlib.model.domain

data class Review(
    val id: String,
    val reviewerId: String,
    val review: String,
    val score: Int,
    val dateOfVisit: String,
    val creationTimeStamp: Long,
    val reply: Reply
)
