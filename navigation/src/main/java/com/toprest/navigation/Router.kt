package com.toprest.navigation

interface Router {

    fun finishHostActivity()

    fun clearAll()

    fun goBack()

    fun showLandingScreen()

    fun showSignUpFlow()

    fun showRegisterName()

    fun showUserTypeSelection()

    fun showRegisterEmail()

    fun showRegisterPassword()

    fun showLogin()

    fun showHome()
}
