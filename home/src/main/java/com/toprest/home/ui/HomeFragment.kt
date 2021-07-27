package com.toprest.home.ui

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewState, FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    companion object {

        const val TAG = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

    override val model : HomeViewModel by viewModel()

    override fun FragmentHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        addRest.setOnClickListener { model.openAddRestaurant() }
        addRev.setOnClickListener { model.openLeaveReview() }
    }
}
