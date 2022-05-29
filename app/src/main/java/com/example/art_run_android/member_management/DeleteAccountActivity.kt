package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.example.art_run_android.DataContainer
import com.example.art_run_android.databinding.MemberManagementActivityDeleteAccountBinding
import com.example.art_run_android.databinding.MemberManagementActivitySelectSettingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeleteAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= MemberManagementActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btBack.setOnClickListener{
            val intent = Intent(this, SelectSettingsActivity::class.java)
            startActivity(intent)
        }

        //레트로핏 생성
        var retrofit= Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스(로그인서비스) 생성
        var loginService=retrofit.create(LoginService::class.java)


        //확인 버튼을 눌렀을 때 비밀번호가 맞다면 서버가 요청하는 데이터클래스를 생성해 보내준다.

        binding.btnDeleteAccountConfirm.setOnClickListener{
            if(binding.textEmail.text.toString()==DataContainer.userEmail){
                loginService.deleteAccount(DataContainer.header,DataContainer.userNumber.toString())
                    .enqueue(object: Callback<Unit> {
                        //통신 실패시 실행되는 코드
                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            //Log.e("회원가입", t.message.toString())
                            Log.e("서버 접속 실패", "${t.localizedMessage}")

                            Toast.makeText(this@DeleteAccountActivity,"통신 실패!", Toast.LENGTH_LONG).show()
                        }

                        //통신 성공시 실행되는 코드
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            //Log.d("SignUp",signupCheck)
                            //val signupCheck=response.body()?.email.toString()
                            var deleteCheck=response
                            Log.d("계정 삭제",deleteCheck.toString())
                            Log.d("계정삭제2",deleteCheck.body().toString())
                            if(response.code()==204 || response.code()==200){
                                Toast.makeText(this@DeleteAccountActivity,"계정이 정상적으로 삭제되었습니다.", Toast.LENGTH_LONG).show()
                            }

                            else if(response.code()==400){
                                Log.d("이메일 중복확인2 : 중복임",response.errorBody()?.string()!!)

                                Toast.makeText(this@DeleteAccountActivity,"계정이 정상적으로 삭제되지 않았습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}