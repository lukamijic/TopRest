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
}
