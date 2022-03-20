package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.art_run_android.databinding.ActivityLoginBinding
import com.example.art_run_android.running.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrofit 만들기
        var retrofit =Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService=retrofit.create(LoginService::class.java)


        binding.btLogin.setOnClickListener{
            val userID=binding.textUserID.text.toString()
            val userPassword=binding.textUserPassword.text.toString()
            val userInfo="ID :"+userID+"\nPassword :"+userPassword

            loginService.requestLogin(userID,userPassword)
                .enqueue(object:Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Toast.makeText(this@LoginActivity,"통신 성공입니다!!",Toast.LENGTH_LONG).show()

                    val login=response.body()?.accessToken
                    Toast.makeText(this@LoginActivity,login+"",Toast.LENGTH_LONG).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }//통신 성공시

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"통신 실패!",Toast.LENGTH_LONG).show()
                }//통신 실패시
            })
