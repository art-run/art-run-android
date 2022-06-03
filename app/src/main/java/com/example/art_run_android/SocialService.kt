package com.example.art_run_android

import retrofit2.Call
import retrofit2.http.*

interface SocialService {

    @GET("/routes")
    fun RecnetRoutesInfo(
        @Header("Authorization") authorization:String?,
        @Query("lastRouteId") lastRouteId: String?
    ) : Call<List<SocialDClass>>

    @GET("/routes/me")
    fun MyRoutesInfo(
        @Header("Authorization") authorization:String?,
        @Query("lastRouteId") lastRouteId: Int?
    ) : Call<List<SocialDClass>>

    @GET("/route/{routeId}")
    fun RouteInfo(
        @Header("Authorization") authorization:String?,
        @Path("routeId") routeId: Int
    ) : Call<List<RouteCtrlDClass>>

}