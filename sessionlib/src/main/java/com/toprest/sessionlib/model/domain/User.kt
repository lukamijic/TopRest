package com.toprest.sessionlib.model.domain

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val userType: UserType
) {

    companion object {

        val EMPTY = User("", "", "", "", UserType.CUSTOMER)
    }

    fun isEmpty() = this == EMPTY
}
