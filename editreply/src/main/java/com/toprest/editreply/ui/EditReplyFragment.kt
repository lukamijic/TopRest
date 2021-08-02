package com.toprest.editreply.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.editreply.databinding.FragmentEditReplyBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class EditReplyFragment : BaseFragment<EditReplyViewState, FragmentEditReplyBinding>(FragmentEditReplyBinding::inflate) {

    companion object {
        const val TAG = "EditReplyFragment"
        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"
        private const val REVIEW_ID_KEY = "REVIEW_ID_KEY"

        fun newInstance(restaurantId: String, reviewId: String) = EditReplyFragment().apply {
            arguments = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                REVIEW_ID_KEY to reviewId
            )
        }
    }

    override val model: EditReplyViewModel by viewModel(parameters = { parametersOf(restaurantId, reviewId) })

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }
    private val reviewId by lazy { requireArguments().getString(REVIEW_ID_KEY) }

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    override fun FragmentEditReplyBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)

        actionSheet.onCollapsedListener = {
            model.close()
        }

        root.onClick { back() }

        actionSheet.onSlideListener = { _, slideOffset ->
            emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset
        }

        reply.doAfterTextChanged { model.setReply(it.toString()) }
        editButton.onThrottledClick { model.edit() }
        delete.onThrottledClick { model.delete() }
    }

    override fun render(viewState: EditReplyViewState) = when(viewState) {
        is EditReplyViewState.ReplyButton -> binding.editButton.enable(viewState.isEnabled)
        is EditReplyViewState.Loading -> binding.plateSpinner.fade(viewState.isVisible)
        is EditReplyViewState.InitialData -> binding.reply.setText(viewState.reply)
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
