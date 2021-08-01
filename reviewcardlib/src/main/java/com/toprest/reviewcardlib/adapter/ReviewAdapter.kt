package com.toprest.reviewcardlib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.toprest.coreui.utils.*
import com.toprest.reviewcardlib.databinding.ItemReviewCardBinding
import com.toprest.reviewcardlib.model.ReviewItem

class ReviewAdapter(
    private val layoutInflater: LayoutInflater,
    private val replyButtonAction: (String) -> Unit
) : BaseListAdapter<ReviewItem, ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ItemReviewCardBinding.inflate(
            layoutInflater,
            parent,
            false
        ),
        replyButtonAction
    )

    class ReviewViewHolder(
        binding: ItemReviewCardBinding,
        private val replyButtonAction: (String) -> Unit
    ) : BaseViewHolder<ReviewItem, ItemReviewCardBinding>(binding) {

        companion object {
            const val DEFAULT_MAX_LINES = 5
        }

        override fun ItemReviewCardBinding.render(item: ReviewItem) {
            date.text = item.date
            score.text = String.format("%d/5", item.score)
            review.text = item.review
            replyButton.show(item.showReplyButton)
            replyButton.onThrottledClick { replyButtonAction(item.id) }

            review.onClick { setMaxLines(review) }
            reply.onClick { setMaxLines(reply) }

            if (item.reply == null) {
                replyContainer.hide()
            } else {
                replyContainer.show()
                reply.text = item.reply.reply
            }
        }

        private fun setMaxLines(textView: TextView) {
            if (textView.maxLines == DEFAULT_MAX_LINES) {
                textView.maxLines = Int.MAX_VALUE
            } else {
                textView.maxLines = DEFAULT_MAX_LINES
            }
        }
    }
}
