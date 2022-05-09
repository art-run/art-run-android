package com.example.art_run_android.running

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class RunningActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_running)

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.mapView3, mapsFragment)
        transaction.commit()

        val pauseButton: ImageButton = findViewById(R.id.pauseButton)
        val startButton: ImageButton = findViewById(R.id.startButton3)
        val cmTimer: Chronometer = findViewById(R.id.runningTime)
        cmTimer.format = "00:%s"
        cmTimer.setOnChronometerTickListener { cArg ->
            val elapsedMillis = SystemClock.elapsedRealtime() - cArg.base
            if (elapsedMillis > 3600000L) {
                cArg.format = "0%s"
            } else {
                cArg.format = "00:%s"
            }
        }

        cmTimer.start()

        startButton.isVisible = false

        pauseButton.setOnClickListener {
            pauseButton.isVisible = false
            startButton.isVisible = true
            cmTimer.stop()
        }
        startButton.setOnClickListener {
            startButton.isVisible = false
            pauseButton.isVisible = true
            cmTimer.start()
        }

        val runningRoutePrim = intent.getStringArrayListExtra("runningRoute")
        val runningRoute = mutableListOf<LatLng>().apply {
            runningRoutePrim?.forEach {
                this.addAll(PolyUtil.decode(it))
            }
        }

        mapsFragment.setMapInitializedListener(object : MapsFragment.MapInitializedListener {
            override fun onMapInitializedEvent() {
                mapsFragment.drawPolyline(runningRoute,false, true)
            }
        })
    }
}