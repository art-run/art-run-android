package com.example.art_run_android.member_management

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.example.art_run_android.DataContainer
import com.example.art_run_android.DataContainer.Companion.header
import com.example.art_run_android.DataContainer.Companion.token
import com.example.art_run_android.DataContainer.Companion.userNumber

import com.example.art_run_android.running.MainActivity
import com.example.art_run_android.databinding.MemberManagementActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MemberManagementActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrofit 만들기
        var retrofit =Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService=retrofit.create(LoginService::class.java)


        binding.btLogin.setOnClickListener{
            val email=binding.textUserID.text.toString()
            val password=binding.textUserPassword.text.toString()
            val userInfo="ID :"+email+"\nPassword :"+password

            val loginDClass=LoginDClass(email,password)

            loginService.requestLogin(loginDClass)
                .enqueue(object:Callback<LoginResponse>{
                    //통신 성공시

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                        val login=response
                        if (login.body().toString()!="null") {
                            Log.d("로그인", login.toString())
                            Log.d("로그인2", login.body().toString())
                            Log.d(
                                "로그인3:accessToken",
                                login.body().toString().split(",")[1].split("=")[1]
                            )
                            var accessToken = login.body().toString().split(",")[1].split("=")[1]

                            token?.accessToken = login.body().toString().split(",")[1].split("=")[1]
                            token?.accessTokenExpiresIn =
                                login.body().toString().split(",")[3].split("=")[1].toInt()
                            token?.grantType = login.body().toString().split(",")[0].split("=")[1]
                            token?.refreshToken =
                                login.body().toString().split(",")[2].split("=")[1]
                            //memberInfo 조회시 필요한 헤더
                            var memberHeader = "Bearer " + accessToken
                            var setAccessToekn = "Bearer {$accessToken}"

                            Log.d("로그인4:memberHeader", memberHeader)

                            header = "Bearer " + accessToken


                            //https://github.com/auth0/JWTDecode.Android 참고
                            // accessToken String에서 필요한 정보를 뽑는다. 우선 JWT객체를 만들어서 memberID에 해당하는 subject(sub)를 뽑아낸다.
                            var userJwt: JWT = JWT(accessToken)
                            var memberId = userJwt.getSubject().toString()
                            Log.d("로그인5:memberId", memberId)

                            userNumber = memberId.toInt()
                            loginService.getMemberInfo(memberHeader, memberId)
                                .enqueue(object : Callback<MemberInfoDClass> {
                                    override fun onResponse(
                                        call: Call<MemberInfoDClass>,
                                        response: Response<MemberInfoDClass>
                                    ) {
                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)

                                        Log.d("로그인6:유저정보 조회", response.body().toString())
                                        //서버로부터 유저 정보를 받아와 변수로 저장함.
                                        var userAge = response.body()?.age.toString().toInt()
                                        var userEmail = response.body()?.email.toString()
                                        var userGender = response.body()?.gender.toString()
                                        var userHeight = response.body()?.height.toString().toInt()
                                        var userNickname = response.body()?.nickname.toString()
                                        var userProfileImg = response.body()?.userProfileImg.toString()
                                        var lastRouteId = userJwt.getSubject().toString();
                                        //var userPassword = response.body()?.password.toString()
                                        var userWeight = response.body()?.weight.toString().toInt()

                                        DataContainer.userAge = userAge
                                        DataContainer.userEmail = userEmail
                                        DataContainer.userGender = userGender
                                        DataContainer.userHeight = userHeight
                                        DataContainer.userNickname = userNickname
                                        DataContainer.userProfileImg = userProfileImg
                                        DataContainer.userWeight = userWeight
//                                        DataContainer.user_id = lastRouteId
                                        DataContainer.memberHeader = setAccessToekn
                                        startActivity(intent)
                                    }

                                    override fun onFailure(
                                        call: Call<MemberInfoDClass>,
                                        t: Throwable
                                    ) {
                                        Log.e("로그인6:유저정보 조회실패", t.localizedMessage)
                                    }
                                })
                        }

                        else {
                            Log.d("로그인실패","아이디 비밀번호 잘못입력")
                            Toast.makeText(this@LoginActivity,"이메일 혹은 비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                    //통신 실패시
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("로그인",t.localizedMessage)
                        Toast.makeText(this@LoginActivity,"통신 실패!",Toast.LENGTH_LONG).show()

                    }//통신 실패시
                })

            val toast = Toast.makeText(this,userInfo,Toast.LENGTH_LONG)
            toast.show()



        }

        binding.btSignup.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}