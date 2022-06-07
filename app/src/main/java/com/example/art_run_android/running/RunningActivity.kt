package com.example.art_run_android.running

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.running.record_card.MakeRecordCard
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.utils.sphericalPathLength
import kotlin.math.roundToInt

class RunningActivity : BaseActivity() {
    lateinit var tracker: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_running)

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.mapView3, mapsFragment)
        transaction.commit()

        val startRouteId = intent.getIntExtra("startRouteID", 0)

        val pausePlayButton: FloatingActionButton = findViewById(R.id.pauseButton)
        val pausePlayView: ImageView = findViewById(R.id.pause_play)
        val pauseToPlay = getDrawable(R.drawable.pause_to_play) as AnimatedVectorDrawable?
        val playToPause = getDrawable(R.drawable.play_to_pause) as AnimatedVectorDrawable?
        var pause = true

        val stopButton: FloatingActionButton = findViewById(R.id.stopButton3)
        val cmTimer: Chronometer = findViewById(R.id.runningTime)
        val distanceTextView: TextView = findViewById(R.id.runningDistTV)
        val speedTextView: TextView = findViewById(R.id.runningSpeedTV)
        val kcalTextView: TextView = findViewById(R.id.runningKcalTV)
        var runningDistance = 0.0
        var runningKcal = 0.0
        var runningSpeed = 0.0
        var runningTime = 0.0
        var pauseTime = 0L
        val finalRoute = mutableListOf<LatLng>()

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
                while(mapsFragment.currentLocation == mapsFragment.defaultLocation){}
                var prim1 : Polyline
                while (!currentThread().isInterrupted) {
                    try {
                        sleep(100)
                        runningTime += 0.1
                        mapsFragment.getMyLocation()
                        finalRoute.add(LatLng(
                            mapsFragment.currentLocation.latitude,
                            mapsFragment.currentLocation.longitude
                        ))
                        runOnUiThread {
                            prim1 = mapsFragment.drawPolyline(finalRoute, false, false)
                            runningDistance = prim1.sphericalPathLength
                            distanceTextView.text = runningDistance.roundToInt().toString() + " m"
                            runningSpeed = (runningDistance / runningTime / 1000) * 3600
                            speedTextView.text = runningSpeed.roundToInt().toString() + " km/h"
                            runningKcal = (runningSpeed * 0.8 * 3.5 * DataContainer.userWeight!! * (runningTime / 60.0) * 5) / 1000
                            kcalTextView.text = runningKcal.roundToInt().toString() + " kcal"
                        }
                    } catch (e: InterruptedException) {
                        currentThread().interrupt()
                    }
                }
            }
        }

        tracker = TrackerThread()
        cmTimer.start()

        pausePlayButton.setOnClickListener {
            val drawable = if (pause) pauseToPlay else playToPause
            pausePlayView!!.setImageDrawable(drawable)
            drawable!!.start()
            pause = !pause


            if(pause) {
                ObjectAnimator.ofFloat(stopButton, "translationY", 0f).apply { start() }
                cmTimer.base = SystemClock.elapsedRealtime() + pauseTime
                cmTimer.start()
                tracker = TrackerThread()
                tracker.start()
            } else {
                ObjectAnimator.ofFloat(stopButton, "translationY", -200f).apply { start() }
                pauseTime = cmTimer.base - SystemClock.elapsedRealtime()
                cmTimer.stop()
                tracker.interrupt()
            }
        }

        stopButton.setOnClickListener {
            val intent = Intent(this, MakeRecordCard::class.java)
            val finalRoutePrim = PolyUtil.encode(finalRoute)
            intent.putExtra("finalRoute", finalRoutePrim)
            intent.putExtra("distance", runningDistance.roundToInt())
            intent.putExtra("kcal", runningKcal.roundToInt())
            intent.putExtra("time", ((SystemClock.elapsedRealtime() - cmTimer.base) / 1000).toInt())
            intent.putExtra("speed",runningSpeed.roundToInt())
            intent.putExtra("startRouteId", startRouteId)
            startActivity(intent)
        }

        val runningRoutePrim = intent.getStringArrayListExtra("runningRoute")
        val runningRoute = mutableListOf<LatLng>().apply {
            runningRoutePrim?.forEach {
                this.addAll(PolyUtil.decode(it))
            }
        }

        mapsFragment.setMapInitializedListener(object : MapsFragment.MapInitializedListener {
            override fun onMapInitializedEvent() {
                mapsFragment.drawPolyline(runningRoute, false, true)
                tracker.start()
            }
        })

    }

    override fun onBackPressed() {
        if(this::tracker.isInitialized) {
            tracker.interrupt()
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        // TODO: 백그라운드 처리하기
        super.onDestroy()
    }
}