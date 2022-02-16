package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.art_run_android.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackToLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btConfirm.setOnClickListener{
            val userEmail=binding.textUserEmail.text.toString()
            val message="유저 이메일 : "+userEmail

            val toast= Toast.makeText(this,message,Toast.LENGTH_LONG)
            toast.show()

            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}