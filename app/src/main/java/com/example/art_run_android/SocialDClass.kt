package com.example.art_run_android

import com.google.gson.annotations.SerializedName
import com.google.maps.android.data.LineString


data class SocialDClass(
    //createdAt   string($date-time)
    //distance   integer($int32)
    //nickname   string
    //profileImg   string
    //routeId   integer($int64)
    //title   string
    //wktRunRoute   string
    @SerializedName("routeId") val routeId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImg") val profileImg: String,
    @SerializedName("title") val title: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("wktRunRoute") val wktRunRoute: String
)
