package com.toprest.editreview.ui

sealed class EditReviewViewState {
    data class Score(val score: Int) : EditReviewViewState()
    data class ReviewButton(val isEnabled: Boolean) : EditReviewViewState()
    data class Date(val date: String) : EditReviewViewState()
    data class Loading(val isLoading: Boolean) : EditReviewViewState()
    data class InitialData(val review: String, val score: Int, val dateOfVisit: String) : EditReviewViewState()
}
