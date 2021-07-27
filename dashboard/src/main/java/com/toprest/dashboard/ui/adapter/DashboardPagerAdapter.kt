package com.toprest.dashboard.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.toprest.home.ui.HomeFragment
import com.toprest.profile.ui.ProfileFragment

private const val DASHBOARD_SCREEN_COUNT = 2

class DashboardPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = DASHBOARD_SCREEN_COUNT

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> HomeFragment.newInstance()
        1 -> ProfileFragment.newInstance()
        else -> throw IllegalArgumentException("There is no screen with index $position")
    }
}
