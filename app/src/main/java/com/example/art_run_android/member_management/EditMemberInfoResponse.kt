package com.example.art_run_android.member_management

data class EditMemberInfoResponse(
    val age: Int,
    val email: String,
    val gender: String,
    val height: Int,
    val nickname: String,
    val profileImg: String,
    val weight: Int
)