package com.example.art_run_android.member_management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.MainActivity
import com.example.art_run_android.databinding.MemberManagementActivityEditAccountInformBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditAccountInform : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=MemberManagementActivityEditAccountInformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener{
            val intent = Intent(this, SelectSettingsActivity::class.java)
            startActivity(intent)
        }

        //멤버 아이디 임시로 초기화
        var memberId=1

        //레트로핏 생성
        var retrofit = Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService=retrofit.create(LoginService::class.java)

        //이메일 수정
        binding.btnEmailFix.setOnClickListener {
            val userEmail = binding.textUserEmail.text.toString()
            //val message="유저 이메일 : "+userEmail
            //val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()

            loginService.editEmail(memberId,userEmail)
                .enqueue(object : Callback<EditMemberInfoResponse> {
                    //통신 성공시

                    override fun onResponse(
                        call: Call<EditMemberInfoResponse>,
                        response: Response<EditMemberInfoResponse>
                    ) {
                        Toast.makeText(this@EditAccountInform, "통신 성공입니다!!", Toast.LENGTH_LONG)
                            .show()

                        val editResponse = response
                        Log.d("정보수정", editResponse.toString())
                        Log.d("정보수정2", editResponse.body().toString())

                    }

                    //통신 실패시
                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("정보수정", t.localizedMessage)
                    }
                })
            }

        //비밀번호 수정
        binding.btnPasswordFix.setOnClickListener{
            val userPassword=binding.password.text.toString()
            //val message="유저 이메일 : "+userEmail
            //val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()

            loginService.editPassword(memberId,userPassword)
                .enqueue(object:Callback<EditMemberInfoResponse>{
                    //통신 성공시

                    override fun onResponse(call: Call<EditMemberInfoResponse>, response: Response<EditMemberInfoResponse>) {
                        Toast.makeText(this@EditAccountInform,"통신 성공입니다!!",Toast.LENGTH_LONG).show()

                        val editResponse=response
                        Log.d("정보수정",editResponse.toString())
                        Log.d("정보수정2",editResponse.body().toString())

                    }
                    //통신 실패시
                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("정보수정",t.localizedMessage)
                    }
                })
            }
        //닉네임 수정
        binding.btnNicknameFIx.setOnClickListener{
            val userNickname=binding.nickName.text.toString()
            //val message="유저 이메일 : "+userEmail
            //val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()

            loginService.editPassword(memberId,userNickname)
                .enqueue(object:Callback<EditMemberInfoResponse>{
                    //통신 성공시

                    override fun onResponse(call: Call<EditMemberInfoResponse>, response: Response<EditMemberInfoResponse>) {
                        Toast.makeText(this@EditAccountInform,"통신 성공입니다!!",Toast.LENGTH_LONG).show()

                        val editResponse=response
                        Log.d("정보수정",editResponse.toString())
                        Log.d("정보수정2",editResponse.body().toString())

                    }
                    //통신 실패시
                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("정보수정",t.localizedMessage)
                    }
                })
        }
    }
}