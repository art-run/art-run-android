package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.art_run_android.databinding.ActivityEditAccountInformBinding

class EditAccountInform : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityEditAccountInformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener{
            val intent = Intent(this,SelectSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btConfirm.setOnClickListener{
            val userEmail=binding.textUserEmail.text.toString()
            val message="유저 이메일 : "+userEmail

            val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            toast.show()

            val intent = Intent(this,SelectSettingsActivity::class.java)
            startActivity(intent)
        }
    }
}