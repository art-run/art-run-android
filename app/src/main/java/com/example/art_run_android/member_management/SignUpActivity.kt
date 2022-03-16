package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.art_run_android.databinding.MemberManagementActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MemberManagementActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btConfirm.setOnClickListener{
            val userEmail=binding.textUserEmail.text.toString()
            val userPassword=binding.password.text.toString()
            val userNickname=binding.nickName.text.toString()


            val intent=Intent(this, SignUpActivity2::class.java)
            intent.putExtra("email",userEmail)
            intent.putExtra("password",userPassword)
            intent.putExtra("nickname",userNickname)
            startActivity(intent)
        }
    }
}