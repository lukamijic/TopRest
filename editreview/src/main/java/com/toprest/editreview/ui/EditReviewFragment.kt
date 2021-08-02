package com.toprest.editreview.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.coinme.formfieldlib.doAfterTextChanged
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.editreview.databinding.FragmentEditReviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class EditReviewFragment : BaseFragment<EditReviewViewState, FragmentEditReviewBinding>(FragmentEditReviewBinding::inflate) {

    companion object {
        const val TAG = "EditReviewFragment"
        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"
        private const val REVIEW_ID_KEY = "REVIEW_ID_KEY"

        fun newInstance(restaurantId: String, reviewId: String) = EditReviewFragment().apply {
            arguments = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                REVIEW_ID_KEY to reviewId
            )
        }
    }

    override val model: EditReviewViewModel by viewModel(parameters = { parametersOf(restaurantId, reviewId) })

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }
    private val reviewId by lazy { requireArguments().getString(REVIEW_ID_KEY) }

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    private val stars by lazy { with(binding) { listOf(star1, star2, star3, star4, star5) } }

    private val datePickerBuilder by lazy {
        val c = Calendar.getInstance(TimeZone.getDefault()).apply {
            timeInMillis = System.currentTimeMillis()
        }
        MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setEnd(c.timeInMillis)
                    .build()
            )
    }

    private val dateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    override fun FragmentEditReviewBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)
        initActionSheet()
        initStars()
        initDateFieldInterceptor()

        review.doAfterTextChanged { model.setReview(it.toString()) }
        editButton.onThrottledClick { model.edit() }
        delete.onThrottledClick { model.delete() }
        root.onClick { back() }
    }

    private fun FragmentEditReviewBinding.initActionSheet() {
        actionSheet.onCollapsedListener = { model.close() }
        actionSheet.onSlideListener = { _, slideOffset -> emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset }
    }

    private fun FragmentEditReviewBinding.initStars() {
        star1.onClick { model.setScore(1) }
        star2.onClick { model.setScore(2) }
        star3.onClick { model.setScore(3) }
        star4.onClick { model.setScore(4) }
        star5.onClick { model.setScore(5) }
    }

    private fun FragmentEditReviewBinding.initDateFieldInterceptor() {
        dateOfVisitFieldClickInterceptor.onThrottledClick {
            datePickerBuilder
                .build().apply {
                    addOnPositiveButtonClickListener { millis ->
                        val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault()).apply {
                            timeInMillis = millis
                        }
                        model.setDate(dateFormat.format(calendar.time))
                    }
                }
                .show(requireActivity().supportFragmentManager, "TAG")
        }
    }
    override fun render(viewState: EditReviewViewState) = when (viewState) {
        is EditReviewViewState.Score -> renderScore(viewState)
        is EditReviewViewState.Date -> renderDate(viewState)
        is EditReviewViewState.ReviewButton -> renderReviewButton(viewState)
        is EditReviewViewState.Loading -> renderLoading(viewState)
        is EditReviewViewState.InitialData -> binding.renderInitialData(viewState)
    }

    private fun renderScore(viewState: EditReviewViewState.Score) {
        for (i in stars.indices) {
            stars[i].isSelected = i + 1 <= viewState.score
        }
    }

    private fun renderDate(viewState: EditReviewViewState.Date) {
        binding.dateOfVisitField.setText(viewState.date)
    }

    private fun renderReviewButton(viewState: EditReviewViewState.ReviewButton) {
        binding.editButton.isEnabled = viewState.isEnabled
    }

    private fun renderLoading(viewState: EditReviewViewState.Loading) {
        binding.plateSpinner.fade(viewState.isLoading)
    }

    private fun FragmentEditReviewBinding.renderInitialData(viewState: EditReviewViewState.InitialData) {
        review.setText(viewState.review)
        model.setDate(viewState.dateOfVisit)
        model.setScore(viewState.score)
    }

    override fun back(): Boolean = binding.actionSheet.close()

}
