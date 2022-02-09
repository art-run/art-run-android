package com.example.art_run_android

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class FreeRunActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freerun)

        val startButton: ImageButton = findViewById(R.id.startButton2)
        startButton.setOnClickListener{
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }
}