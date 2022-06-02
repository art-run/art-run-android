package com.example.art_run_android.running

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.google.android.gms.maps.model.Polyline
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.running_activity_courserun.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseRunActivity : BaseActivity() {

    lateinit var courseAdapter: CourseAdapter
    val recommendedRoutes = mutableListOf<RecommendedRoute>()
    private val mapsFragment = MapsFragment()
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_courserun)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.mapView3, mapsFragment)
        transaction.commit()

        initRecycler()

        textView = findViewById(R.id.infoTextView)
        val confirmButton: ImageButton = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            if(mapsFragment.undoPolylineList.isNotEmpty()) {
                val intent = Intent(this, RunningActivity::class.java)
                val runningRoute = ArrayList<String>().apply {
                    mapsFragment.undoPolylineList.forEach {
                        this.add(PolyUtil.encode(it.points))
                    }
                }
                var queryMap = mapOf("memberId" to "0", "wktTargetRoute" to polylineToWkt(mapsFragment.undoPolylineList))
                val callPostRouteStart = ArtRunClient.routeApiService.postRouteStart(DataContainer.header,queryMap)

                callPostRouteStart.enqueue(object : Callback<RouteId>{
                    override fun onResponse(call: Call<RouteId>, response: Response<RouteId>) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            Log.d("post route start","통신 성공")
                            val routeId = response.body() as RouteId

                            //intent.putExtra("routeID", routeId.routeId)


                        } else { // code == 400
                            Log.d("post route start","통신 실패 : " + response.errorBody()?.string()!!)
                        }
                    }

                    override fun onFailure(call: Call<RouteId>, t: Throwable) {
                        Log.d("post route start", "통신 실패 : $t")
                    }

                })

                intent.putExtra("runningRoute", runningRoute)
                startActivity(intent)

            } else {
                //경로를 선택해주세요
            }
        }
    }

    private fun initRecycler() {
        courseAdapter = CourseAdapter(this)
        val rvCourse : RecyclerView = findViewById(R.id.recycler_course)
        rvCourse.adapter = courseAdapter

        val distance = intent.getDoubleExtra("distance",0.0)

        var queryMap1 = mapOf("distance" to distance.toInt().toString(), "offset" to "0",
            "pageNumber" to "0")
        val callGetRecommendationList = ArtRunClient.recommendationApiService.getRecommendationList(DataContainer.header,queryMap1)

        callGetRecommendationList.enqueue(object : Callback<List<RecommendedRoute>> {
            override fun onResponse(
                call: Call<List<RecommendedRoute>>,
                response: Response<List<RecommendedRoute>>
            ) {
                if (response.isSuccessful) { // <--> response.code == 200
                    Log.d("경로 추천","통신 성공")
                    recommendedRoutes.addAll(response.body() as List<RecommendedRoute>)
                    courseAdapter.recommendedRoutes = recommendedRoutes
                    courseAdapter.notifyDataSetChanged()

                } else { // code == 400
                    Log.d("경로 추천","통신 실패")
                }
            }

            override fun onFailure(call: Call<List<RecommendedRoute>>, t: Throwable) {
                Log.d("경로 추천", "통신 실패 : $t")
            }
        })

        courseAdapter.setOnItemClickListener(object : CourseAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: RecommendedRoute) {
                val currentLocation = mapsFragment.currentLocation
                val queryMap2 = mapOf("lat" to currentLocation.latitude , "lng" to currentLocation.longitude)
                val callGetRecommendation = ArtRunClient.recommendationApiService.getRecommendation(DataContainer.header,data.id,queryMap2)
                callGetRecommendation.enqueue(object : Callback<RecommendedRoute> {
                    override fun onResponse(
                        call: Call<RecommendedRoute>,
                        response: Response<RecommendedRoute>
                    ) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            Log.d("경로 추천 보간","통신 성공")
                            val route = response.body() as RecommendedRoute
                            val routePrim = courseAdapter.wktToPolyline(route.wktRoute)
                            mapsFragment.undoPolyline()
                            mapsFragment.drawPolyline(routePrim,false, false)
                            infoTextView.text = "제목 ${data.title}\n거리: ${data.distance}m \n예상 소요 시간: 분"

                        } else { // code == 400
                            Log.d("경로 추천 보간","통신 실패 : ")
                        }
                    }

                    override fun onFailure(call: Call<RecommendedRoute>, t: Throwable) {
                        Log.d("경로 추천 보간", "통신 실패 : $t")
                    }
                })

            }
            })

    }

    private fun polylineToWkt(polylineList: List<Polyline>): String {
        var sb= StringBuilder()
        sb.append("LINESTRING (")


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
        sb.append(")")

        return sb.toString()
    }
}