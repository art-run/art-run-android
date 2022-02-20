package com.example.art_run_android

data class LoginResponse(
    val accessToken: String,
    val accessTokenExpiresIn: Int,
    val grantType: String,
    val refreshToken: String
)