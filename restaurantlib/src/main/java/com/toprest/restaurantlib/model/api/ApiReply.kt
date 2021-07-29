package com.toprest.restaurantlib.model.api

import com.google.firebase.database.IgnoreExtraProperties
import com.toprest.restaurantlib.model.domain.Reply

@IgnoreExtraProperties
data class ApiReply(val reply: String? = null, val creationTimestamp: Long? = null)
