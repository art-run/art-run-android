package com.example.art_run_android

import com.google.gson.annotations.SerializedName


data class SocialDClass(
    @SerializedName("routeId") val routeId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("title") val title: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("wktRunRoute") val wktRunRoute: String
)
