package com.example.art_run_android.running

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseRunActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_courserun)

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.mapView3, mapsFragment)
        transaction.commit()

        val distance = intent.getDoubleExtra("distance",0.0)
        val currentLocation = intent.getParcelableExtra<LatLng>("currentLocation")

        var queryMap = mapOf("distance" to distance.toInt().toString(), "offset" to "0",
            "pageNumber" to "0", "pageSize" to "10", "paged" to "true", "sort.sorted" to "true",
        "sort.unsorted" to "false", "unpaged" to "true")
        val callGetRecommendation = RecommendationClient.recommendationApiService.getRecommendation(DataContainer.header,queryMap)

        callGetRecommendation.enqueue(object : Callback<List<RecommendedRoute>> {
            override fun onResponse(
                call: Call<List<RecommendedRoute>>,
                response: Response<List<RecommendedRoute>>
            ) {
                if (response.isSuccessful) { // <--> response.code == 200
                    Log.d("경로 추천","통신 성공")
                    val recRoutes = response.body() as List<RecommendedRoute>
                    Log.d("경로 추천","추천 갯수: ${recRoutes.size}")
                    recRoutes.forEach {
                        val title = it.title
                        println("제목 : $title")
                    }

                } else { // code == 400
                    Log.d("경로 추천","통신 실패")
                }
            }

            override fun onFailure(call: Call<List<RecommendedRoute>>, t: Throwable) {
                Log.d("경로 추천", "통신 실패 : $t")
            }
        })

        val confirmButton: ImageButton = findViewById(R.id.confirm_button)
        confirmButton.setOnClickListener {
            val intent = Intent(this, RunningActivity::class.java)
            startActivity(intent)
        }
    }
}