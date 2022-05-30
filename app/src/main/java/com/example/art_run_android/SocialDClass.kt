package com.example.art_run_android

import com.google.gson.annotations.SerializedName

data class SocialDClass(
    //createdAt   string($date-time)
    //distance   integer($int32)
    //nickname   string
    //profileImg   string
    //routeId   integer($int64)
    //title   string
    //wktRunRoute   string
    @SerializedName("createdAT") val createdAt: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("routeId") val routeId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("wktRunRoute") val wktRunRoute: String
)
