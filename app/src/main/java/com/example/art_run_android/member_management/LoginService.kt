package com.example.art_run_android.member_management

import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @POST("/auth/login")
    fun requestLogin(
        //인풋 정의
        @Body loginDClass: LoginDClass
    ) : Call<LoginResponse>  //아웃풋 정의

    //@FormUrlEncoded
    @POST("/auth/signup")
    fun signUp(
        @Body memberInfoDClass: MemberInfoDClass
    ) : Call<SignUpResponse>
    /*
    처음에 시도한 방식, 이것은 인터페이스에서 Body가 아닌 Field로 보내는 경우 쓰임. 비교를 위해 남겨둠.
    Body형식으로 쓸 때는 @FormUrlEncoded를 지워주자.

    fun signUp(
        @Field ("age") age: Int,
        @Field("email") email:String,
        @Field("gender") gender:String,
        @Field("height") height:Int,
        @Field("nickname") nickname:String,
        @Field("password") password:String,
        @Field("weight") weight:Int,
        ) : Call<SignUpResponse>
    */

    @GET("/member/{memberId}")
    fun getMemberInfo(
        @Header("Authorization") authorization:String?,
        @Path("memberId") memberId:String,
        ) : Call<MemberInfoDClass>


    @PUT("/member/{memberId}")
    fun editBodyInfo(
        @Header("Authorization") authorization:String?,
        @Path("memberId") memberId: String,
        @Body editAccountInfoDClass:EditAccountInfoDClass
    ) : Call<EditMemberInfoResponse>


    @PUT("/member/{memberId}")
    fun editEmail(
        @Header("Authorization") authorization:String?,
        @Path ("memberId") memberId:String,
        @Body editAccountInfoDClass:EditAccountInfoDClass
    ) : Call<EditMemberInfoResponse>



    @PUT("/member/{memberId}")
    fun editNickname(
        @Header("Authorization") authorization:String?,
        @Path("memberId") memberId: Int,
        @Body editAccountInfoDClass:EditAccountInfoDClass
    ) : Call<EditMemberInfoResponse>

    @FormUrlEncoded
    @PUT("/member/{memberId}")
    fun editPassword(
        @Header("Authorization") authorization:String?,
        @Path("memberId") memberId: Int,
        @Field("password") password: String
    ) : Call<EditMemberInfoResponse>

}