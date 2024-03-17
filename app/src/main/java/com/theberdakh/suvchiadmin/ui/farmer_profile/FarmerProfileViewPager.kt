package com.theberdakh.suvchiadmin.ui.farmer_profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FarmerProfileViewPager(private val fragments: List<Fragment>, private val fragmentManager: FragmentManager, val lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]

}