package com.example.art_run_android.running

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.google.android.gms.maps.model.Polyline
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
                                mapsFragment.drawPolyline(polyline, false, false)
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

        val toolbar: Toolbar = findViewById(R.id.toolbar2)
        val undoButton: ImageButton = findViewById(R.id.undoButton)
        undoButton.setOnClickListener {
            mapsFragment.undoPolyline()
        }

        val redoButton: ImageButton = findViewById(R.id.redoButton)
        redoButton.setOnClickListener {
            mapsFragment.redoPolyline()
        }

        val mapButton: ImageButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener {
            if (drawRouteView.isVisible) {
                mapButton.setImageResource(R.drawable.ic_outline_draw_24)
                redoButton.isVisible = false
                undoButton.isVisible = false
                drawRouteView.isVisible = false
                toolbar.subtitle = "지도 위치를 조정하세요."
            } else {
                mapButton.setImageResource(R.drawable.ic_baseline_map_24)
                redoButton.isVisible = true
                undoButton.isVisible = true
                drawRouteView.isVisible = true
                toolbar.subtitle = "지도 위에 경로 그림을 그려주세요."
            }
        }

        val startButton: ImageButton = findViewById(R.id.startButton2)
        startButton.setOnClickListener {
            if (mapsFragment.undoPolylineList.isNotEmpty()) {
                val intent = Intent(this, RunningActivity::class.java)
                val runningRoute = ArrayList<String>().apply {
                    mapsFragment.undoPolylineList.forEach {
                        this.add(PolyUtil.encode(it.points))
                    }
                }
                val requestBody = RouteStartRequest(DataContainer.userNumber,polylineToWkt(mapsFragment.undoPolylineList))
                val callPostRouteStart = ArtRunClient.routeApiService.postRouteStart(DataContainer.header,requestBody)

                callPostRouteStart.enqueue(object : Callback<RouteId>{
                    override fun onResponse(call: Call<RouteId>, response: Response<RouteId>) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            val startRouteId = response.body() as RouteId
                            Log.d("post route start", "통신 성공 : $startRouteId")

                            intent.putExtra("startRouteID", startRouteId.routeId)
                            intent.putExtra("runningRoute", runningRoute)
                            startActivity(intent)

                        } else { // code == 400
                            Log.d("post route start","통신 실패 : " + response.errorBody()?.string()!!)
                        }
                    }

                    override fun onFailure(call: Call<RouteId>, t: Throwable) {
                        Log.d("post route start", "통신 실패 : $t")
                    }

                })
            } else {
                //그림그려주세요
            }
        }
    }

    private fun polylineToWkt(polylineList: List<Polyline>): String {
        var sb= StringBuilder()
        sb.append("LINESTRING (")


        for (i in polylineList.indices) {
            val polyline = polylineList[i].points
            for (j in polyline.indices){
                sb.append(polyline[j].longitude.toString())
                sb.append(" ")
                sb.append(polyline[j].latitude.toString())
                if(!((i == polylineList.size-1)&&(j == polyline.size-1))) {
                    sb.append(", ")
                }
            }
        }
        /*
        polylineList.forEach{ polyline ->
            polyline.points.forEach {
                sb.append(it.longitude.toString())
                sb.append(" ")
                sb.append(it.latitude.toString())
                if(it != polylineList.last().points.last()) {
                    sb.append(", ")
                }
            }
        }
         */
        sb.append(")")

        return sb.toString()
    }
}





