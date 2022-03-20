package com.example.art_run_android.member_management

data class LoginResponse(
    /*
    val accessToken: String,
    val accessTokenExpiresIn: Int,
    val grantType: String,
    val refreshToken: String
    */
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long
)