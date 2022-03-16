package com.example.art_run_android.member_management

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    //var email: String
    @SerializedName("email") var email: String?

)