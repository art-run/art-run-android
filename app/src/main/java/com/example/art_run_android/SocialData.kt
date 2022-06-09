package com.example.art_run_android

import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.data.LineString


data class SocialData (
    val profileImg : String,
    val nickname : String,
    val title : String,
    val distance : String,
    val createdAt : String,
    val polylineOptions : PolylineOptions
    )
