package com.example.art_run_android.member_management

import com.google.gson.annotations.SerializedName

data class LoginDClass(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)