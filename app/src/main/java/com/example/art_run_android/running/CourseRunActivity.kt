package com.example.art_run_android.running

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R

class CourseRunActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_courserun)
        val confirmButton: ImageButton = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }
}