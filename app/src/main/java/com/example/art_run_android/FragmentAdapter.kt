package com.example.art_run_android


import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MysocialFragment()
            else -> RecentsocialFragment()
            //else -> Error()
        }
    }
}