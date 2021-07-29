package com.top.restaurantcardlib.model

import com.toprest.coreui.utils.DiffUtilViewModel

data class RestaurantOverview(
    override val id: String,
    val name: String,
    val description: String,
    val averageScore: Float?
) :  DiffUtilViewModel(id)
