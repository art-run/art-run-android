package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.art_run_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener{
            val userID=binding.textUserID.text.toString()
            val userPassword=binding.textUserPassword.text.toString()
            val userInfo="ID :"+userID+"\nPassword :"+userPassword

            val toast = Toast.makeText(this,userInfo,Toast.LENGTH_LONG)
            toast.show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.btSignup.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}