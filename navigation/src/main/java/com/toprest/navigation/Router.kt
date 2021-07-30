package com.toprest.navigation

interface Router {

    fun finishHostActivity()

    fun clearAll()

    fun goBack()

    fun showStartActivity()

    fun showMainActivity()

    fun showLandingScreen()

    fun showSignUpFlow()

    fun showRegisterName()

    fun showUserTypeSelection()

    fun showRegisterEmail()

    fun showRegisterPassword()

    fun showLogin()

    fun showDashboard()

    fun showAddRestaurant()

    fun closeAddRestaurant()

    fun showLeaveReview(restaurantId: String)

    fun closeLeaveReview()

    fun showReviewReply(restaurantId: String, reviewId: String)

    fun closeReviewReply()

    fun showCustomerHome()

    fun showRestaurantDetails(restaurantId: String)
}
