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

    fun showEditRestaurant(restaurantId: String)

    fun closeEditRestaurant()

    fun showLeaveReview(restaurantId: String)

    fun closeLeaveReview()

    fun showEditReview(restaurantId: String, reviewId: String)

    fun closeEditReview()

    fun showReviewReply(restaurantId: String, reviewId: String)

    fun closeReviewReply()

    fun showEditReply(restaurantId: String, reviewId: String)

    fun closeEditReply()

    fun showCustomerHome()

    fun showOwnerHome()

    fun showAdminHome()

    fun showRestaurantDetails(restaurantId: String)

    fun closeRestaurantDetails()

    fun showFilterHome()

    fun closeFilterHome()
}
