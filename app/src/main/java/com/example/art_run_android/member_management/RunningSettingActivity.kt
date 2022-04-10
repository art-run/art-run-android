package com.example.art_run_android.member_management

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.databinding.MemberManagementActivityRunningSettingBinding

class RunningSettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding=MemberManagementActivityRunningSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btBack.setOnClickListener{
            val intent = Intent(this, SelectSettingsActivity::class.java)
            startActivity(intent)
        }
        val imageButton=binding.imgBtnProfile
        val url= DataContainer.userProfileImg
        Glide.with(this).load(url).into(imageButton)

        binding.textNickname.text= DataContainer.userNickname
    }
}