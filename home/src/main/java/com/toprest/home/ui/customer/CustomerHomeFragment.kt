package com.toprest.home.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import com.top.restaurantcardlib.adapter.RestaurantAdapter
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fadeOut
import com.toprest.home.databinding.FragmentCustomerHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerHomeFragment : BaseFragment<CustomerHomeViewState, FragmentCustomerHomeBinding>(FragmentCustomerHomeBinding::inflate) {

    companion object {
        const val TAG = "CustomerHomeFragment"

        fun newInstance() = CustomerHomeFragment()
    }

    override val model: CustomerHomeViewModel by viewModel()

    private val adapter by lazy { RestaurantAdapter(LayoutInflater.from(context)) { model.openRestaurantDetails(it) } }

    override fun FragmentCustomerHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        restaurants.adapter = adapter
    }

    override fun render(viewState: CustomerHomeViewState) {
        adapter.submitList(viewState.restaurantOverviews)
        binding.plateSpinner.fadeOut()
    }
}
