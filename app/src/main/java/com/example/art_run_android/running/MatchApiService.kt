package com.example.art_run_android.running

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MatchApiService {
    @GET("/routed-foot/route/v1/driving/{coordinates}")
    fun getMatch(
        @Path("coordinates")
        coordinates: String
    ) : Call<RouteDataClass>
}

object OsrmClient {
    private val retrofitClient : Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("https://routing.openstreetmap.de/")
            .addConverterFactory(GsonConverterFactory.create())
    }
    val matchApiService: MatchApiService by lazy {
        retrofitClient.build().create(MatchApiService::class.java)
    }
}

