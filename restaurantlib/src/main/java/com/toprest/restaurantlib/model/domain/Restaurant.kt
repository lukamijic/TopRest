package com.toprest.restaurantlib.model.domain

data class Restaurant(
    val id: String,
    val ownerId: String,
    val name: String,
    val description: String,
    val reviews: List<Review>
)
