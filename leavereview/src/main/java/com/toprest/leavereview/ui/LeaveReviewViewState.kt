package com.toprest.leavereview.ui

sealed class LeaveReviewViewState {
    data class Score(val score: Int) : LeaveReviewViewState()
    data class ReviewButton(val isEnabled: Boolean) : LeaveReviewViewState()
    data class Date(val date: String) : LeaveReviewViewState()
    data class Loading(val isLoading: Boolean) : LeaveReviewViewState()
}
