package com.toprest.restaurantlib.model.api

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ApiReview(
    val id: String? = null,
    val restaurantId: String? = null,
    val reviewerId: String? = null,
    val review: String? = null,
    val score: Int? = null,
    val dateOfVisit: String? = null,
    val creationTimestamp: Long? = null,
    val reply: ApiReply? = null
)
