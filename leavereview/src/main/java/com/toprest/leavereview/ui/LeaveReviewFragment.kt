package com.toprest.leavereview.ui

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
import com.toprest.leavereview.databinding.FragmentLeaveReviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class LeaveReviewFragment : BaseFragment<LeaveReviewViewState, FragmentLeaveReviewBinding>(FragmentLeaveReviewBinding::inflate) {

    companion object {
        const val TAG = "ReviewFragment"
        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"

        fun newInstance(restaurantId: String) = LeaveReviewFragment().apply {
            arguments = bundleOf(RESTAURANT_ID_KEY to restaurantId)
        }
    }

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }

    override val model: LeaveReviewViewModel by viewModel(parameters = { parametersOf(restaurantId) })

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

    override fun FragmentLeaveReviewBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)
        initActionSheet()
        initStars()
        initDateFieldInterceptor()

        review.doAfterTextChanged { model.setReview(it.toString()) }
        reviewButton.onThrottledClick { model.review() }
        root.onClick { back() }
    }

    private fun FragmentLeaveReviewBinding.initActionSheet() {
        actionSheet.onCollapsedListener = { model.close() }
        actionSheet.onSlideListener = { _, slideOffset -> emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset }
    }

    private fun FragmentLeaveReviewBinding.initStars() {
        star1.onClick { model.setScore(1) }
        star2.onClick { model.setScore(2) }
        star3.onClick { model.setScore(3) }
        star4.onClick { model.setScore(4) }
        star5.onClick { model.setScore(5) }
    }

    private fun FragmentLeaveReviewBinding.initDateFieldInterceptor() {
        dateOfVisitFieldClickInterceptor.onThrottledClick {
            datePickerBuilder
                .build().apply {
                    addOnPositiveButtonClickListener { millis ->
                        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
                            timeInMillis = millis
                        }
                        model.setDate(dateFormat.format(calendar.time))
                    }
                }
                .show(requireActivity().supportFragmentManager, "TAG")
        }
    }

    override fun render(viewState: LeaveReviewViewState) = when (viewState) {
        is LeaveReviewViewState.Score -> renderScore(viewState)
        is LeaveReviewViewState.Date -> renderDate(viewState)
        is LeaveReviewViewState.ReviewButton -> renderReviewButton(viewState)
        is LeaveReviewViewState.Loading -> renderLoading(viewState)
    }

    private fun renderScore(viewState: LeaveReviewViewState.Score) {
        for (i in stars.indices) {
            stars[i].isSelected = i + 1 <= viewState.score
        }
    }

    private fun renderDate(viewState: LeaveReviewViewState.Date) {
        binding.dateOfVisitField.setText(viewState.date)
    }

    private fun renderReviewButton(viewState: LeaveReviewViewState.ReviewButton) {
        binding.reviewButton.isEnabled = viewState.isEnabled
    }

    private fun renderLoading(viewState: LeaveReviewViewState.Loading) {
        binding.plateSpinner.fade(viewState.isLoading)
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
