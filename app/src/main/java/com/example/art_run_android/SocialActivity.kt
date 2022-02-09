package com.example.art_run_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
//import kotlinx.android.synthetic.main.activity_social.*


class SocialActivity : BaseActivity() {

    private val tabTextList = arrayListOf("최근 기록", "내 기록")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)
       /* init()*/
    }

/*
    private fun init() {
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2) {
                tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }
*/

}