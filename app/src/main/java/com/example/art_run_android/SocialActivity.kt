package com.example.art_run_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.*
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_social.*
import kotlinx.android.synthetic.main.fragment_recentsocial.*


class SocialActivity : BaseActivity() {

    private val tabTextList = arrayListOf("최근 기록", "내 기록")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)
        init()
    }

    private fun init() {
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2) {
                tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

//    private fun getData() {// 임시데이터
//        val profileList = arrayListOf(
//            SocialData("김ㅇㅇ", "asdf", "3", "2022-02-13" ),
//            SocialData("이ㅇㅇ", "adsf22", "1.5", "2022-02-13"),
//            SocialData("박ㅇㅇ", "qwer", "11" , "2022-12-12"),
//            SocialData("한ㅇㅇ", "qwer44", "0", "2021-11-11"),
//            SocialData("김ㅇㅇ", "asdf", "3", "2022-02-13" ),
//            SocialData("이ㅇㅇ", "adsf22", "1.5", "2022-02-13"),
//        )
//
//        rv_profile.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rv_profile.setHasFixedSize(true)
//        rv_profile.adapter = RecyclerAdapter_Social(profileList)
//
//    }

}