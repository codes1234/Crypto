package com.akguptaboy.crypto.Adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akguptaboy.crypto.Fragments.TopGainLoseFragment

class ToplosgainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
    return 2
    }

    override fun createFragment(position: Int): Fragment {
val fragment = TopGainLoseFragment()
    val bundle = Bundle()
        bundle.putInt("position",position)
        fragment.arguments=bundle
        return fragment

    }
}