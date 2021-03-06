package com.toprest.sessionlib.model.api

import com.google.firebase.database.IgnoreExtraProperties
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType

@IgnoreExtraProperties
data class ApiUser(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val userType: String? = null
) {

    companion object {
        val EMPTY = ApiUser()
    }
}

fun ApiUser.toUser() = if (isEmpty()) {
    User.EMPTY
} else {
    User(
        id!!,
        firstName!!,
        lastName!!,
        email!!,
        UserType.fromName(userType!!)
    )
}

fun ApiUser.isEmpty() = this === ApiUser.EMPTY
