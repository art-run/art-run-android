package com.example.art_run_android.member_management

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Gallery
import com.bumptech.glide.Glide
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.DataContainer.Companion.userNickname
import com.example.art_run_android.DataContainer.Companion.userProfileImg
import com.example.art_run_android.R
import com.example.art_run_android.databinding.MemberManagementActivitySelectSettingsBinding
import com.example.art_run_android.running.MainActivity

class SelectSettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding=MemberManagementActivitySelectSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val imageButton=binding.imgBtnProfile
        val url=userProfileImg
        Glide.with(this).load(url).into(imageButton)

        binding.textNickname.text= userNickname


        val button6: Button = findViewById(R.id.bt_bodyInfoEdit)
        val button7: Button = findViewById(R.id.bt_editAccountInfo)
        val button8: Button = findViewById(R.id.bt_runningSetting)
        button6.setOnClickListener {
            startActivity(Intent(this, BodyInformEditActivity::class.java))
        }
        button7.setOnClickListener {
            startActivity(Intent(this, EditAccountInform::class.java))
        }
        button8.setOnClickListener {
            startActivity(Intent(this, RunningSettingActivity::class.java))
        }

        binding.imgBtnProfile.setOnClickListener{
            val Gallery=0
            val intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, Gallery)
        }

        binding.btLogout.setOnClickListener{
            DataContainer.clear()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btDeleteAccount.setOnClickListener{
            val intent = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intent)
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            var currentImageUrl: Uri?=data?.data
            Log.d("이미지 정보 불러오기","$currentImageUrl")
        }
        else{
            Log.d("이미지 정보 불러오기","Wrong")
        }
    }

}