package com.toprest.sessionlib.model.domain

enum class UserType {
    CUSTOMER,
    OWNER,
    ADMIN;

    companion object {

        fun fromName(name: String) = when (name) {
            CUSTOMER.name -> CUSTOMER
            OWNER.name -> OWNER
            ADMIN.name -> ADMIN
            else -> throw IllegalArgumentException("Invalid user type")
        }
    }
}
