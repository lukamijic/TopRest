package com.toprest.restaurantdetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.coreui.utils.show
import com.toprest.restaurantdetails.databinding.FragmentRestaurantDetailsBinding
import com.toprest.reviewcardlib.adapter.ReviewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val DEFAULT_MAX_LINES = 7

class RestaurantDetailsFragment :
    BaseFragment<RestaurantDetailsViewState, FragmentRestaurantDetailsBinding>(FragmentRestaurantDetailsBinding::inflate) {

    companion object {

        const val TAG = "RestaurantDetailsFragment"
        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"

        fun newInstance(restaurantId: String) = RestaurantDetailsFragment().apply {
            arguments = bundleOf(RESTAURANT_ID_KEY to restaurantId)
        }
    }

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }
    private val adapter by lazy { ReviewAdapter(LayoutInflater.from(requireContext())) { model.reply(it) } }

    override val model: RestaurantDetailsViewModel by viewModel(parameters = { parametersOf(restaurantId) })

    override fun FragmentRestaurantDetailsBinding.initialiseView(savedInstanceState: Bundle?) {
        reviews.adapter = adapter

        reviewButton.onThrottledClick { model.leaveReview() }
        description.onClick {
            if (description.maxLines == DEFAULT_MAX_LINES) {
                description.maxLines = Int.MAX_VALUE
            } else {
                description.maxLines = DEFAULT_MAX_LINES
            }
        }
    }

    override fun render(viewState: RestaurantDetailsViewState) = when(viewState) {
        is RestaurantDetailsViewState.Details -> binding.renderRestaurantDetails(viewState)
        is RestaurantDetailsViewState.ReviewButton -> binding.renderReviewButton(viewState)
        is RestaurantDetailsViewState.Reviews -> renderReviews(viewState)
    }

    private fun FragmentRestaurantDetailsBinding.renderRestaurantDetails(viewState: RestaurantDetailsViewState.Details) {
        name.text = viewState.name
        description.text = viewState.description
        score.text = viewState.score
    }

    private fun FragmentRestaurantDetailsBinding.renderReviewButton(viewState: RestaurantDetailsViewState.ReviewButton) {
        reviewButton.show(viewState.isVisible)
    }

    private fun renderReviews(viewState: RestaurantDetailsViewState.Reviews) {
        adapter.submitList(viewState.reviewsItems)
    }
}
