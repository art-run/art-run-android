package com.example.art_run_android.running

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RecommendationApiService {
    @GET("/recommendations")
    fun getRecommendation(
        @Header("Authorization") authorization:String?,
        @QueryMap par: Map<String, String>
    ): Call<List<RecommendedRoute>>
}

object RecommendationClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
    }
    val recommendationApiService: RecommendationApiService by lazy {
        retrofitClient.build().create(RecommendationApiService::class.java)
    }
}