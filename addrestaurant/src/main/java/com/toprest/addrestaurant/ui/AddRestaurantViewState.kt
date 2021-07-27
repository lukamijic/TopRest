package com.toprest.addrestaurant.ui

sealed class AddRestaurantViewState {
    class Button(val isEnabled: Boolean) : AddRestaurantViewState()
    class Loading(val isVisible: Boolean) : AddRestaurantViewState()
}
