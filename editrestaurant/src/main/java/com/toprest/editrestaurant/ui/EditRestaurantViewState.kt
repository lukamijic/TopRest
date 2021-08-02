package com.toprest.editrestaurant.ui

sealed class EditRestaurantViewState {
    data class InitialData(val restaurantName: String, val restaurantDescription: String): EditRestaurantViewState()
    data class Button(val isEnabled: Boolean) : EditRestaurantViewState()
    data class Loading(val isVisible: Boolean) : EditRestaurantViewState()
}
