package com.toprest.ui.registerpassword

sealed class RegisterPasswordViewState {
    class Password(val password: String) : RegisterPasswordViewState()
    class RepeatedPassword(val repeatedPassword: String) : RegisterPasswordViewState()
    class PasswordError(val isVisible: Boolean) : RegisterPasswordViewState()
    class RepeatedPasswordError(val isVisible: Boolean) : RegisterPasswordViewState()
    class AccountCreationError(val isVisible: Boolean) : RegisterPasswordViewState()
}
