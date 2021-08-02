package com.toprest.edituser.ui

import com.toprest.sessionlib.model.domain.UserType

sealed class EditUserViewState {
    data class InitialData(val userType: UserType, val firstName: String, val lastName: String) : EditUserViewState()
    data class IsLoading(val isLoading: Boolean) :  EditUserViewState()
    data class EditButton(val isEnabled: Boolean) :  EditUserViewState()
}
