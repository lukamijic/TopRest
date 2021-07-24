package com.toprest.ui.signup

sealed class SignUpViewState {
    data class Title(val title: String) : SignUpViewState()
    data class ActionButtonText(val text: String) : SignUpViewState()
    data class ActionButtonEnabled(val isEnabled: Boolean) : SignUpViewState()
    data class Loading(val isLoading: Boolean) : SignUpViewState()
}
