package com.toprest.dashboard.ui

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.dashboard.databinding.FragmentDashboardBinding
import com.toprest.dashboard.ui.adapter.DashboardPagerAdapter
import com.toprest.dashboard.ui.pagertransformer.DashboardPagerTransformer
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<DashboardViewState, FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    companion object {
        const val TAG = "DashboardFragment"

        fun newInstance() = DashboardFragment()
    }

    private val viewPagerAdapter by lazy { DashboardPagerAdapter(this) }
    private val viewPagerPageTransformer by lazy { DashboardPagerTransformer() }

    override val model: DashboardViewModel by viewModel()

    override fun FragmentDashboardBinding.initialiseView(savedInstanceState: Bundle?) {
        initViewPager()
        initTabBar()
    }

    private fun FragmentDashboardBinding.initViewPager() {
        viewPager.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = viewPagerAdapter.itemCount
            isUserInputEnabled = false
            setPageTransformer(viewPagerPageTransformer)
        }
    }

    private fun FragmentDashboardBinding.initTabBar() {
        tabBar.setOnStateChangedListener(model::positionSelected)
    }

    override fun render(viewState: DashboardViewState) {
        binding.viewPager.setCurrentItem(viewState.screenPosition, true)
        binding.tabBar.select(viewState.screenPosition)
    }

    override fun onDestroy() {
        binding.viewPager.adapter = null
        super.onDestroy()
    }
}
