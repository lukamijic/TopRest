package com.toprest.editreply.ui

sealed class EditReplyViewState {
    class ReplyButton(val isEnabled: Boolean) : EditReplyViewState()
    class Loading(val isVisible: Boolean) : EditReplyViewState()
    class InitialData(val reply: String) : EditReplyViewState()
}
