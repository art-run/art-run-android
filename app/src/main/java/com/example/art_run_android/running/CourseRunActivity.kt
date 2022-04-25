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
import com.google.android.gms.maps.model.LatLng
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
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycler() {
        courseAdapter = CourseAdapter(this)
        val rvCourse : RecyclerView = findViewById(R.id.recycler_course)
        rvCourse.adapter = courseAdapter

        val distance = intent.getDoubleExtra("distance",0.0)

        var queryMap1 = mapOf("distance" to distance.toInt().toString(), "offset" to "0",
            "pageNumber" to "0"/*, "pageSize" to "10", "paged" to "true", "sort.sorted" to "true",
            "sort.unsorted" to "false", "unpaged" to "true"*/)
        val callGetRecommendationList = RecommendationClient.recommendationApiService.getRecommendationList(DataContainer.header,queryMap1)

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
                val callGetRecommendation = RecommendationClient.recommendationApiService.getRecommendation(DataContainer.header,data.id,queryMap2)

                callGetRecommendation.enqueue(object : Callback<RecommendedRoute> {
                    override fun onResponse(
                        call: Call<RecommendedRoute>,
                        response: Response<RecommendedRoute>
                    ) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            Log.d("경로 추천 보간","통신 성공")
                            val route = response.body() as RecommendedRoute
                            val routePrim = wktToPolyline(route.wktRoute)
                            mapsFragment.drawPolyline(routePrim,false)
                            infoTextView.text = "제목 ${data.title}\n거리: ${data.distance}km \n예상 소요 시간: 분"

                        } else { // code == 400
                            Log.d("경로 추천 보간","통신 실패")
                        }
                    }

                    override fun onFailure(call: Call<RecommendedRoute>, t: Throwable) {
                        Log.d("경로 추천 보간", "통신 실패 : $t")
                    }
                })

            }
            })

    }

    private fun wktToPolyline(wktRoute: String): MutableList<LatLng> {
        val prim1 = wktRoute.substring(wktRoute.indexOf('(') + 1, wktRoute.indexOf(')'))
        val prim2 = prim1.split(", ")
        val polyline = mutableListOf<LatLng>()
        prim2.forEach {
            val prim3 = it.split(" ")
            if (prim3.size >= 2) {
                val latLng = LatLng(prim3[1].toDouble(), prim3[0].toDouble())
                polyline.add(latLng)
            }
        }

        return polyline
    }
}