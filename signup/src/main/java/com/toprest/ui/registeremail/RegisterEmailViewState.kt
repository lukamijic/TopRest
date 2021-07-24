package com.toprest.ui.registeremail

sealed class RegisterEmailViewState {
    data class Email(val email: String) : RegisterEmailViewState()
    data class Error(val showError: Boolean) : RegisterEmailViewState()
}
