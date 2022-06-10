package com.example.art_run_android.running

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

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
        @Body routeStartRequest: RouteStartRequest
    ): Call<RouteId>

    @POST("/route/finish")
    fun postRouteFinish(
        @Header("Authorization") authorization: String?,
        @Body routeFinishRequest: RouteFinishRequest
    ): Call<RouteId>

    @GET("/route/{routeId}")
    fun getRoute(
        @Header("Authorization") authorization: String?,
        @Path("routeId") routeId: Int
    ): Call<CompleteRoute>

    @DELETE("/route/{routeId}")
    fun deleteRoute(
        @Header("Authorization") authorization: String?,
        @Path("routeId") routeId: Int
    ): Call<DELETE>
}

object ArtRunClient {
    private val httpLoggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val okHttpClient : OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
    }

    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }
    val recommendationApiService: RecommendationApiService by lazy {
        retrofitClient.build().create(RecommendationApiService::class.java)
    }
    val routeApiService: RouteApiService by lazy {
        retrofitClient.build().create(RouteApiService::class.java)
    }

}