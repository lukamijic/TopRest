package com.tempo.reply.ui

sealed class ReviewReplyViewState {
    class ReplyButton(val isEnabled: Boolean) : ReviewReplyViewState()
    class Loading(val isVisible: Boolean) : ReviewReplyViewState()
}
