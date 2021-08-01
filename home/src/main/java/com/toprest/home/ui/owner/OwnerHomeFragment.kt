package com.toprest.home.ui.owner

import android.os.Bundle
import android.view.LayoutInflater
import com.top.restaurantcardlib.adapter.RestaurantAdapter
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.fadeOut
import com.toprest.coreui.utils.onClick
import com.toprest.home.R
import com.toprest.home.databinding.FragmentOwnerHomeBinding
import com.toprest.reviewcardlib.adapter.ReviewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class OwnerHomeFragment : BaseFragment<OwnerHomeViewState, FragmentOwnerHomeBinding>(FragmentOwnerHomeBinding::inflate) {

    companion object {
        const val TAG = "OwnerHomeFragment"

        fun newInstance() = OwnerHomeFragment()
    }

    override val model: OwnerHomeViewModel by viewModel()

    private val restaurantAdapter by lazy { RestaurantAdapter(LayoutInflater.from(context), model::showRestaurantDetails) }

    private val reviewAdapter by lazy { ReviewAdapter(LayoutInflater.from(context), (model::showReplyReview)) }

    override fun FragmentOwnerHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        restaurants.adapter = restaurantAdapter
        reviews.adapter = reviewAdapter

        screenType.onClick { model.changeScreenType() }
    }

    override fun render(viewState: OwnerHomeViewState) = when(viewState) {
        is OwnerHomeViewState.Title -> binding.renderTitle(viewState)
        is OwnerHomeViewState.Restaurants -> binding.renderRestaurants(viewState)
        is OwnerHomeViewState.PendingReviews -> binding.renderPendingReviews(viewState)
        is OwnerHomeViewState.ScreenType -> binding.renderScreenType(viewState)
    }

    private fun FragmentOwnerHomeBinding.renderTitle(viewState: OwnerHomeViewState.Title) {
        title.text = viewState.title
    }

    private fun FragmentOwnerHomeBinding.renderRestaurants(viewState: OwnerHomeViewState.Restaurants) {
        restaurantAdapter.submitList(viewState.restaurants)
        plateSpinner.fadeOut()
    }

    private fun FragmentOwnerHomeBinding.renderPendingReviews(viewState: OwnerHomeViewState.PendingReviews) {
        reviewAdapter.submitList(viewState.reviews)
        plateSpinner.fadeOut()
    }

    private fun FragmentOwnerHomeBinding.renderScreenType(viewState: OwnerHomeViewState.ScreenType) {
        restaurants.fade(viewState.screenType == OwnerHomeScreenType.RESTAURANTS)
        reviews.fade(viewState.screenType == OwnerHomeScreenType.REVIEWS)

        if (viewState.screenType == OwnerHomeScreenType.RESTAURANTS) {
            screenType.setImageResource(R.drawable.ic_reply)
        } else {
            screenType.setImageResource(R.drawable.ic_restaurant)
        }
    }
}
