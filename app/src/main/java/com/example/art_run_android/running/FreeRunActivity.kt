package com.example.art_run_android.running

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeRunActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_freerun)

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.mapView2, mapsFragment)
        transaction.commit()

        val drawRouteView = DrawRouteView(this)
        val drawRouteLayout: FrameLayout = findViewById(R.id.drawRouteLayout)
        drawRouteLayout.addView(drawRouteView)
        drawRouteView.SetStrokeListener(object : DrawRouteView.StrokeListener {
            override fun onStrokeEvent(pointList: MutableList<Point>) {
                val list = mapsFragment.getLatLngList(pointList)
                val string = StringBuilder()
                list.forEach {
                    string.append(it.longitude)
                    string.append(",")
                    string.append(it.latitude)
                    if (list.indexOf(it) != list.size - 1) {
                        string.append(";")
                    }
                }

                val callGetMatch = OsrmClient.matchApiService.getMatch(string.toString())

                callGetMatch.enqueue(object : Callback<RouteDataClass> {
                    override fun onResponse(
                        call: Call<RouteDataClass>,
                        response: Response<RouteDataClass>
                    ) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            val match = response.body() as RouteDataClass
                            match.routes.forEach {
                                val polyline = PolyUtil.decode(it.geometry)
                                mapsFragment.drawPolyline(polyline, false)
                            }

                        } else { // code == 400
                            // 실패 처리
                        }
                    }

                    override fun onFailure(call: Call<RouteDataClass>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })


            }
        })


        val undoButton: ImageButton = findViewById(R.id.undoButton)
        undoButton.setOnClickListener {
            mapsFragment.undoPolyline()
        }

        val redoButton: ImageButton = findViewById(R.id.redoButton)
        redoButton.setOnClickListener {
            mapsFragment.redoPolyline()
        }

        val startButton: ImageButton = findViewById(R.id.startButton2)
        startButton.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }

}





