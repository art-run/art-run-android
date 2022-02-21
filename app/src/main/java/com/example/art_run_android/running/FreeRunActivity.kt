package com.example.art_run_android.running

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R


class FreeRunActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freerun)

        val drawRouteView = DrawRouteView(this)
        val drawRouteLayout : LinearLayout = findViewById(R.id.drawRouteLayout)
        drawRouteLayout.addView(drawRouteView)

        val undoButton : ImageButton = findViewById(R.id.undoButton)
        undoButton.setOnClickListener{
            drawRouteView.undo()
        }

        val redoButton : ImageButton = findViewById(R.id.redoButton)
        redoButton.setOnClickListener{
            drawRouteView.redo()
        }

        val startButton: ImageButton = findViewById(R.id.startButton2)
        startButton.setOnClickListener{
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }
}


