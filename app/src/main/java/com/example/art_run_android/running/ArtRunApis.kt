package com.example.art_run_android.running

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RecommendationApiService {
    @GET("/recommendations")
    fun getRecommendationList(
        @Header("Authorization") authorization: String?,
        @QueryMap par: Map<String, String>
    ): Call<List<RecommendedRoute>>

    @GET("/recommendation/{recommendationId}/adjust")
    fun getRecommendation(
        @Header("Authorization") authorization: String?,
        @Path("recommendationId") recommendationId: Int,
        @QueryMap par: Map<String, Double>
    ): Call<RecommendedRoute>
}

interface RouteApiService {
    @POST("/route/start")
    fun postRouteStart(
        @Header("Authorization") authorization: String?,
        @QueryMap par: Map<String, String?>
    ): Call<RouteId>

    @POST("/route/finish")
    fun postRouteFinish(
        @Header("Authorization") authorization: String?,
        @QueryMap par: Map<String, String?>
    ): Call<RouteId>
}

object ArtRunClient {
    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
    }
    val recommendationApiService: RecommendationApiService by lazy {
        retrofitClient.build().create(RecommendationApiService::class.java)
    }
    val routeApiService: RouteApiService by lazy {
        retrofitClient.build().create(RouteApiService::class.java)
    }
}