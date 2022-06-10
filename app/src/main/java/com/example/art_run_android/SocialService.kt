package com.example.art_run_android

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SocialService {

    @GET("/routes")
    fun getRecentRoutes(
        @Header("Authorization") authorization:String?,
        @Query("lastRouteId") lastRouteId: String?
    ) : Call<List<SocialDClass>>

    @GET("/routes/me")
    fun getMyRoutes(
        @Header("Authorization") authorization:String?,
        @Query("lastRouteId") lastRouteId: String?
    ) : Call<List<SocialDClass>>
}

object SocialClient {
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
    val socialService: SocialService by lazy {
        retrofitClient.build().create(SocialService::class.java)
    }
}