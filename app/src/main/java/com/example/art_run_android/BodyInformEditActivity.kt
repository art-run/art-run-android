package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.art_run_android.databinding.ActivityBodyInformEditBinding

class BodyInformEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityBodyInformEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener{
            val intent = Intent(this,SelectSettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btConfirm.setOnClickListener{
            val height=binding.textHeight.text.toString()
            val weight=binding.textWeight.text.toString()
            val age = binding.textAge.text.toString()

            val message = "키 : "+height+"cm\n몸무게 : "+weight+"kg\n나이 : "+age

            val toast = Toast.makeText(this,message,Toast.LENGTH_LONG)
            toast.show()
            val intent = Intent(this,SelectSettingsActivity::class.java)
            startActivity(intent)
        }

    }
}