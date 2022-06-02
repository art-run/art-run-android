package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.art_run_android.databinding.MemberManagementActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MemberManagementActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //레트로핏생성
        var retrofit= Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스(로그인서비스) 생성
        var loginService=retrofit.create(LoginService::class.java)

        binding.btDuplicationCheck.setOnClickListener{
            var email=binding.textUserEmail.text.toString()
            loginService.emailDuplicationCheck(email)
                .enqueue(object: Callback<Unit> {
                //통신 실패시 실행되는 코드
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    //Log.e("회원가입", t.message.toString())
                    Log.e("이메일 중복확인 체크 실패", "${t.localizedMessage}")

                    Toast.makeText(this@SignUpActivity,"통신 실패!", Toast.LENGTH_LONG).show()
                }

                //통신 성공시 실행되는 코드
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    //Log.d("SignUp",signupCheck)
                    //val signupCheck=response.body()?.email.toString()
                    var emailDuplicationCheck=response
                    Log.d("이메일 중복화인",emailDuplicationCheck.toString())
                    Log.d("이메일 중복확인2",emailDuplicationCheck.body().toString())
                    if(response.code()==200){
                        Toast.makeText(this@SignUpActivity,"해당 이메일을 사용하실 수 있습니다.", Toast.LENGTH_LONG).show()
                    }

                    else if(response.code()==400){
                        Log.d("이메일 중복확인2 : 중복임",response.errorBody()?.string()!!)

                        Toast.makeText(this@SignUpActivity,"다른 이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            )
        }

        binding.btConfirm.setOnClickListener{
            val userEmail=binding.textUserEmail.text.toString()
            val userPassword=binding.password.text.toString()
            val userNickname=binding.nickName.text.toString()


            val intent=Intent(this, SignUpActivity2::class.java)
            intent.putExtra("email",userEmail)
            intent.putExtra("password",userPassword)
            intent.putExtra("nickname",userNickname)
            startActivity(intent)
        }
    }
}