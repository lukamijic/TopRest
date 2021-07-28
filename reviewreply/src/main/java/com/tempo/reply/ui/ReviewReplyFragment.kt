package com.tempo.reply.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.coinme.formfieldlib.doAfterTextChanged
import com.tempo.reply.databinding.FragmentReviewReplyBinding
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class ReviewReplyFragment : BaseFragment<ReviewReplyViewState, FragmentReviewReplyBinding>(FragmentReviewReplyBinding::inflate) {

    companion object {

        const val TAG = "ReviewReplyFragment"
        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"
        private const val REVIEW_ID_KEY = "REVIEW_ID_KEY"

        fun newInstance(restaurantId: String, reviewId: String) = ReviewReplyFragment().apply {
            arguments = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                REVIEW_ID_KEY to reviewId
            )
        }
    }

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }
    private val reviewId by lazy { requireArguments().getString(REVIEW_ID_KEY) }

    override val model: ReviewReplyViewModel by viewModel(parameters = { parametersOf(restaurantId, reviewId) })

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    override fun FragmentReviewReplyBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)
        initActionSheet()

        reply.doAfterTextChanged { model.setReply(it.toString()) }
        replyButton.onThrottledClick { model.reply() }
        root.onClick { back() }
    }

    private fun FragmentReviewReplyBinding.initActionSheet() {
        actionSheet.onCollapsedListener = { model.close() }
        actionSheet.onSlideListener = { _, slideOffset -> emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset }
    }

    override fun render(viewState: ReviewReplyViewState) = when (viewState) {
        is ReviewReplyViewState.ReplyButton -> renderReplyButton(viewState)
        is ReviewReplyViewState.Loading -> renderLoading(viewState)
    }

    private fun renderReplyButton(viewState: ReviewReplyViewState.ReplyButton) {
        binding.replyButton.isEnabled = viewState.isEnabled
    }

    private fun renderLoading(viewState: ReviewReplyViewState.Loading) {
        binding.plateSpinner.fade(viewState.isVisible)
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
