package com.example.art_run_android.member_management

import com.google.gson.annotations.SerializedName

data class ErrorCodeDClass(
    @SerializedName("code") val code: String,
    @SerializedName("errors") val errors: String,
    @SerializedName("message") val message: String
)