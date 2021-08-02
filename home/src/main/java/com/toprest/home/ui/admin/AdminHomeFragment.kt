package com.toprest.home.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayout
import com.top.restaurantcardlib.adapter.RestaurantAdapter
import com.toprest.commonui.utils.TabSelectedAdapter
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.fadeOut
import com.toprest.home.databinding.FragmentAdminHomeBinding
import com.toprest.reviewcardlib.adapter.ReviewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminHomeFragment : BaseFragment<AdminHomeViewState, FragmentAdminHomeBinding>(FragmentAdminHomeBinding::inflate) {

    companion object {
        const val TAG = "AdminHomeFragment"

        fun newInstance() = AdminHomeFragment()
    }

    override val model: AdminHomeViewModel by viewModel()

    private val restaurantAdapter by lazy { RestaurantAdapter(LayoutInflater.from(requireContext()), model::showRestaurantDetails) }

    private val reviewAdapter by lazy { ReviewAdapter(LayoutInflater.from(requireContext()), {}) { } }

    override fun FragmentAdminHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        recyclerView.adapter = restaurantAdapter

        tabs.apply {
            clipToOutline = true
            addOnTabSelectedListener(TabSelectedAdapter(tabSelected = ::handleTabSelected))
        }
    }

    override fun render(viewState: AdminHomeViewState) = when(viewState) {
        is AdminHomeViewState.Restaurants -> binding.renderRestaurants(viewState)
        is AdminHomeViewState.Reviews -> binding.renderReviews(viewState)
        is AdminHomeViewState.SelectedTab -> binding.renderSelectedTab(viewState)
    }

    private fun FragmentAdminHomeBinding.renderRestaurants(viewState: AdminHomeViewState.Restaurants) {
        plateSpinner.fadeOut()
        restaurantAdapter.submitList(viewState.restaurants)
    }

    private fun FragmentAdminHomeBinding.renderReviews(viewState: AdminHomeViewState.Reviews) {
        plateSpinner.fadeOut()
        reviewAdapter.submitList(viewState.reviews)
    }

    private fun FragmentAdminHomeBinding.renderSelectedTab(viewState: AdminHomeViewState.SelectedTab) {
        recyclerView.adapter = when(viewState.tab) {
            Tab.RESTAURANT -> restaurantAdapter
            Tab.REVIEWS -> reviewAdapter
        }
    }

    private fun handleTabSelected(tab: TabLayout.Tab?) {
        tab?.position?.let {
            model.setTab(
                when(it) {
                    0 -> Tab.RESTAURANT
                    1 -> Tab.REVIEWS
                    else -> throw IllegalArgumentException("Invalid tab")
                }
            )
        }
    }
}
