package com.example.art_run_android

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SocialFragment(false)
            else -> SocialFragment(true)
        }
    }
}