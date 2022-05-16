package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.art_run_android.databinding.MemberManagementActivityDeleteAccountBinding
import com.example.art_run_android.databinding.MemberManagementActivitySelectSettingsBinding

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= MemberManagementActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener{
            val intent = Intent(this, SelectSettingsActivity::class.java)
            startActivity(intent)
        }

        //확인 버튼을 눌렀을 때 비밀번호가 맞다면 서버가 요청하는 데이터클래스를 생성해 보내준다.
    }
}