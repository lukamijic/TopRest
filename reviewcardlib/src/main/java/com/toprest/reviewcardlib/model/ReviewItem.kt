package com.toprest.reviewcardlib.model

import com.toprest.coreui.utils.DiffUtilViewModel

data class ReviewItem(
    override val id: String,
    val score: Int,
    val date: String,
    val review: String,
    val showReplyButton: Boolean,
    val reply: ReplyItem?
) : DiffUtilViewModel(id)

data class ReplyItem(
    val reply: String
)
