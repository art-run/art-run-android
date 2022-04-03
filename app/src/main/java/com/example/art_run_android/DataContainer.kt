package com.example.art_run_android

class DataContainer {
    companion object {
        var userAge: Int? = null
        var userEmail: String? = null
        var userGender: String? = null
        var userHeight: Int? = null
        var userNickname: String? = null
        var userProfileImg: String? = null
        var userPassword: String? = null
        var userWeight: Int? = null
        var token: Token? = null
        var userNumber: Int=0    //멤버 정보 조회시 memberId에 들어가는 정보
        var header: String?=null    //멤버 정보 조회시 header에 들어가야하는 정보

        fun clear() {
            userAge = null
            userEmail = null
            userGender = null
            userHeight = null
            userNickname = null
            userProfileImg = null
            userPassword = null
            userWeight = null
            token = null
            userNumber = 0
            header = null
        }
    }
}

class Token {
    var accessToken: String? = null
    var accessTokenExpiresIn: Int? = null
    var grantType: String? = null
    var refreshToken: String? = null
}