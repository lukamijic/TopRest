package com.toprest.home.ui

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.home.databinding.FragmentHomeBinding
import com.toprest.home.ui.adapter.HomePagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewState, FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    companion object {

        const val TAG = "HomeFragment"

        fun newInstance() = HomeFragment()
    }

    override val model : HomeViewModel by viewModel()

    private val homeAdapter by lazy { HomePagerAdapter(this) }

    override fun FragmentHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        viewPager.apply {
            adapter = homeAdapter
            isUserInputEnabled = false
        }
    }

    override fun render(viewState: HomeViewState) {
        binding.viewPager.setCurrentItem(viewState.userType.ordinal, false)
    }
}
