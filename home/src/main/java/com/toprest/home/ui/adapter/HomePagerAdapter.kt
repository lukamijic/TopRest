package com.toprest.home.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.toprest.home.ui.admin.AdminHomeFragment
import com.toprest.home.ui.customer.CustomerHomeFragment
import com.toprest.home.ui.owner.OwnerHomeFragment

private const val HOME_SCREEN_COUNT = 3

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = HOME_SCREEN_COUNT

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> CustomerHomeFragment.newInstance()
        1 -> OwnerHomeFragment.newInstance()
        2 -> AdminHomeFragment.newInstance()
        else -> throw IllegalArgumentException("There is no screen with index $position")
    }
}
