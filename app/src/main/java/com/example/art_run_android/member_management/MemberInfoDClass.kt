package com.example.art_run_android.member_management

import com.google.gson.annotations.SerializedName

/*
data class MemberInfoDClass(
    val age: Int,
    val email: String,
    val gender: String,
    val height: Int,
    val nickname: String,
    val password: String,
    val weight: Int
)
처음에 시도한 방식, 이것은 인터페이스에서 Body가 아닌 Field로 보내는 경우 쓰임. 비교를 위해 남겨둠.
 */
data class MemberInfoDClass(
    @SerializedName("age") val age: Int,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("height") val height: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImg") val userProfileImg: String,
    //@SerializedName("password") val password: String,
    @SerializedName("weight") val weight: Int
)