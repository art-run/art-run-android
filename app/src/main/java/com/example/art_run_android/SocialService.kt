package com.example.art_run_android

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SocialService {

    @GET("/routes")
    fun RecnetRoutesInfo(
        @Header("Authorization") authorization:String?,
        @Query("lastRouteId") lastRouteId: String?
    ) : Call<List<SocialDClass>>
}