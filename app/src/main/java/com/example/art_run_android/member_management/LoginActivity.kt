package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.auth0.android.jwt.JWT

import com.example.art_run_android.MainActivity
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
                        Log.d("로그인",login.toString())
                        Log.d("로그인2",login.body().toString())
                        Log.d("로그인3:accessToken",login.body().toString().split(",")[1].split("=")[1])
                        var accessToken=login.body().toString().split(",")[1].split("=")[1]

                        //memberInfo 조회시 필요한 헤더
                        var memberHeader="Bearer "+accessToken
                        Log.d("로그인4:memberHeader",memberHeader)

                        //https://github.com/auth0/JWTDecode.Android 참고
                        // accessToken String에서 필요한 정보를 뽑는다. 우선 JWT객체를 만들어서 memberID에 해당하는 subject(sub)를 뽑아낸다.
                        var userJwt: JWT= JWT(accessToken)
                        var memberId=userJwt.getSubject().toString()
                        Log.d("로그인5:memberId",memberId)

                        loginService.getMemberInfo(memberHeader,memberId)
                            .enqueue(object :Callback<MemberInfoDClass>{
                                override fun onResponse(
                                    call: Call<MemberInfoDClass>,
                                    response: Response<MemberInfoDClass>
                                ) {
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

                                    Log.d("로그인6:유저정보 조회",response.body().toString())
                                    //서버로부터 유저 정보를 받아와 변수로 저장함.
                                    var userAge=response.body()?.age.toString()
                                    var userEmail=response.body()?.email.toString()
                                    var userGender=response.body()?.gender.toString()
                                    var userHeight=response.body()?.height.toString().toInt()
                                    var userNickname=response.body()?.nickname.toString()
                                    var userPassword=response.body()?.password.toString()
                                    var userWeight=response.body()?.weight.toString().toInt()
                                    intent.putExtra("Age",userAge)
                                    intent.putExtra("Email",userEmail)
                                    intent.putExtra("Gender",userGender)
                                    intent.putExtra("Height",userHeight)
                                    intent.putExtra("Nickname",userNickname)
                                    intent.putExtra("Password",userPassword)
                                    intent.putExtra("Weight",userWeight)
                                    startActivity(intent)


                                }

                                override fun onFailure(call: Call<MemberInfoDClass>, t: Throwable) {
                                    Log.e("로그인6:유저정보 조회실패",t.localizedMessage)
                                }
                            })

                        /*
                        if(login.body()?.grantType=="bearer"){}
                         */


                }
                    //통신 실패시
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("로그인",t.localizedMessage)
                        //Toast.makeText(this@LoginActivity,"통신 실패!",Toast.LENGTH_LONG).show()
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