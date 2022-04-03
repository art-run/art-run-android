package com.example.art_run_android.member_management

import com.google.gson.annotations.SerializedName

data class EditAccountInfoDClass(
    @SerializedName("age") var age: Int?,
    @SerializedName("email") var email: String?,
    @SerializedName("gender") var gender: String?,
    @SerializedName("height") var height: Int?,
    @SerializedName("nickname")var nickname: String?,
    @SerializedName("profileImg")var userProfileImg: String?,
    @SerializedName("weight")var weight: Int?,
    //val password: String? 비밀번호 변경은 아직 구현 X
)