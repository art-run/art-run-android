package com.example.art_run_android

class DataContainer {
    companion object {
        var userAge: String? = null
        var userEmail: String? = null
        var userGender: String? = null
        var userHeight: Int? = null
        var userNickname: String? = null
        var userPassword: String? = null
        var userWeight: Int? = null
        var token: Token? = null

        fun clear() {
            userAge = null
            userEmail = null
            userGender = null
            userHeight = null
            userNickname = null
            userPassword = null
            userWeight = null
            token = null
        }
    }
}

class Token {
    var accessToken: String? = null
    var accessTokenExpiresIn: Int? = null
    var grantType: String? = null
    var refreshToken: String? = null
}