package com.toprest.login.ui

sealed class LoginViewState {
    data class LoginButton(val enabled: Boolean) : LoginViewState()
    data class EmailError(val isVisible: Boolean) : LoginViewState()
    data class PasswordError(val isVisible: Boolean) : LoginViewState()
    data class LoginError(val isVisible: Boolean) : LoginViewState()
    data class Loading(val isLoading: Boolean) : LoginViewState()
}
