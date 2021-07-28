package com.toprest.restaurantlib.model.api

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ApiRestaurant(
    val id: String? = null,
    val ownerId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val reply: ApiReply? = null
)
