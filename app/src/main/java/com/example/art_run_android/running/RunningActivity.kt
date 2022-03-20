package com.example.art_run_android.running

import android.os.Bundle
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R

class RunningActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_running)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.mapView3, MapsFragment())
        transaction.commit()

        val pauseButton: ImageButton = findViewById(R.id.pauseButton)
        val startButton: ImageButton = findViewById(R.id.startButton3)
        startButton.isVisible = false
        startButton.isClickable = false

        pauseButton.setOnClickListener{
            pauseButton.isVisible = false
            startButton.isVisible = true
        }
        startButton.setOnClickListener{
            startButton.isVisible = false
            pauseButton.isVisible = true
        }
    }
}