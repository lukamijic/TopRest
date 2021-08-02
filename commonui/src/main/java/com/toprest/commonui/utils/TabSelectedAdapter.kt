package com.toprest.commonui.utils

import com.google.android.material.tabs.TabLayout

open class TabSelectedAdapter(
    private val tabReselected: (tab: TabLayout.Tab?) -> Unit = { },
    private val tabUnselected: (tab: TabLayout.Tab?) -> Unit = { },
    private val tabSelected: (tab: TabLayout.Tab?) -> Unit = { }
) : TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) = tabReselected(tab)

    override fun onTabUnselected(tab: TabLayout.Tab?) = tabUnselected(tab)

    override fun onTabSelected(tab: TabLayout.Tab?) = tabSelected(tab)
}
