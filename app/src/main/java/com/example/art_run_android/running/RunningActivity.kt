package com.example.art_run_android.running

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.utils.sphericalPathLength
import kotlin.math.roundToInt

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
        val distanceTextView:TextView = findViewById(R.id.runningDistTV)
        val speedTextView:TextView = findViewById(R.id.runningSpeedTV)
        val kcalTextView:TextView = findViewById(R.id.runningKcalTV)
        var runningDistance = 0.0
        var runningKcal = 0.0
        var runningSpeed: Double
        var runningTime = 0
        var pauseTime = 0L

        cmTimer.format = "00:%s"

        cmTimer.setOnChronometerTickListener { cArg ->
            val elapsedMillis = SystemClock.elapsedRealtime() - cArg.base
            if (elapsedMillis > 3600000L) {
                cArg.format = "0%s"
            } else {
                cArg.format = "00:%s"
            }
        }


        class TrackerThread : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e : InterruptedException) {
                    currentThread().interrupt()
                }
                var trackSpot = LatLng(mapsFragment.currentLocation.latitude,mapsFragment.currentLocation.longitude)
                while (!currentThread().isInterrupted) {
                    try {
                        sleep(2000)
                        runningTime += 3
                        mapsFragment.getMyLocation()
                        val trackRoute = listOf(
                            LatLng(trackSpot.latitude, trackSpot.longitude),
                            LatLng(
                                mapsFragment.currentLocation.latitude,
                                mapsFragment.currentLocation.longitude
                            )
                        )
                        trackSpot = LatLng(
                            mapsFragment.currentLocation.latitude,
                            mapsFragment.currentLocation.longitude
                        )
                        runOnUiThread {
                            val prim1 = mapsFragment.drawPolyline(trackRoute, false, false)
                            runningDistance += prim1.sphericalPathLength
                            distanceTextView.text = runningDistance.roundToInt().toString() + " m"
                            runningSpeed = (runningDistance / runningTime / 1000) * 3600
                            speedTextView.text = runningSpeed.roundToInt().toString() + " km/h"
                            runningKcal += 0.3
                            kcalTextView.text = runningKcal.roundToInt().toString() + "kcal"
                        }
                        runningTime += 3
                    } catch (e : InterruptedException) {
                        currentThread().interrupt()
                    }
                }
            }
        }

        var tracker : Thread = TrackerThread()
        cmTimer.start()

        startButton.isVisible = false

        pauseButton.setOnClickListener {
            pauseButton.isVisible = false
            startButton.isVisible = true
            pauseTime = cmTimer.base - SystemClock.elapsedRealtime()
            cmTimer.stop()
            tracker.interrupt()
        }

        startButton.setOnClickListener {
            startButton.isVisible = false
            pauseButton.isVisible = true
            cmTimer.base = SystemClock.elapsedRealtime() + pauseTime
            cmTimer.start()
            tracker=TrackerThread()
            tracker.start()
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
                tracker.start()
            }
        })
    }
}