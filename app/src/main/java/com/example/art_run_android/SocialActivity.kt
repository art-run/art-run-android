package com.example.art_run_android

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.social_activity.*


class SocialActivity : BaseActivity() {

    private val tabTextList = arrayListOf("최근 기록", "내 기록")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.social_activity)
        init()
    }

    private fun init() {
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2) {
                tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

}