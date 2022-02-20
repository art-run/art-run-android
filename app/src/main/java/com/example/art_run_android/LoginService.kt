package com.example.art_run_android

import android.provider.ContactsContract
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @FormUrlEncoded
    @POST("/auth/login")
    fun requestLogin(
        //인풋 정의
        @Field("email") email: String,
        @Field("password") password:String
    ) : Call<LoginResponse>  //아웃풋 정의
}