package com.example.art_run_android

import android.os.Bundle
import android.widget.ImageButton
import androidx.core.view.isVisible

class RunningActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running)

        val pauseButton: ImageButton = findViewById(R.id.pauseButton)
        val startButton: ImageButton = findViewById(R.id.startButton3)
        startButton.isVisible = false
        startButton.isClickable = false


        pauseButton.setOnClickListener{
            pauseButton.isVisible = false
            pauseButton.isClickable = false
            startButton.isVisible = true
            startButton.isClickable = true
        }
        startButton.setOnClickListener{
            startButton.isVisible = false
            startButton.isClickable = false
            pauseButton.isVisible = true
            pauseButton.isClickable = true
        }
    }
}