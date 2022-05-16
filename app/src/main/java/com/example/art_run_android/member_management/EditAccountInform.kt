package com.example.art_run_android.member_management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.DataContainer.Companion.header
import com.example.art_run_android.DataContainer.Companion.userAge
import com.example.art_run_android.DataContainer.Companion.userEmail
import com.example.art_run_android.DataContainer.Companion.userGender
import com.example.art_run_android.DataContainer.Companion.userHeight
import com.example.art_run_android.DataContainer.Companion.userNickname
import com.example.art_run_android.DataContainer.Companion.userNumber
import com.example.art_run_android.DataContainer.Companion.userProfileImg
import com.example.art_run_android.DataContainer.Companion.userWeight
import com.example.art_run_android.running.MainActivity
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
        val imageButton=binding.imgBtnProfile
        val url=userProfileImg
        Glide.with(this).load(url).into(imageButton)

        binding.textNickname.text= userNickname

        //레트로핏 생성
        var retrofit = Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginService=retrofit.create(LoginService::class.java)

        //이메일 수정
        binding.btnEmailFix.setOnClickListener {
            val userEmail = binding.textUserEmail.text.toString()
            val editAccountInfoDClass=EditAccountInfoDClass(userAge,userEmail, userGender, userHeight,
                userNickname, userProfileImg, userWeight)

            loginService.editEmail(header, userNumber.toString(),editAccountInfoDClass)
                .enqueue(object : Callback<EditMemberInfoResponse>{
                    override fun onResponse(
                        call: Call<EditMemberInfoResponse>,
                        response: Response<EditMemberInfoResponse>
                    ) {
                        Log.d("계정정보수정 : 이메일 변경",response.body()?.email.toString())
                        val builder = AlertDialog.Builder(this@EditAccountInform)
                        builder.setTitle("수정 완료").
                        setMessage("정보 수정이 완료되었습니다.")
                        builder.show()
                    }


                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("계정정보수정 : 이메일 변경","${t.localizedMessage}")
                    }
                }

            )

        }
        binding.btnPasswordFix.setOnClickListener{
            Toast.makeText(this,"비밀번호 변경은 아직 구현되지 않았습니다.",Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this@EditAccountInform)
            builder.setTitle("준비중").
            setMessage("비밀번호 수정기능은 준비중입니다.")
            builder.show()
        }
        /*비밀번호 수정은 아직 구현되지 않음
        //비밀번호 수정
        binding.btnPasswordFix.setOnClickListener{
            val userPassword=binding.password.text.toString()
            //val message="유저 이메일 : "+userEmail
            //val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()

            loginService.editPassword(header, userNumber,userPassword)
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

         */
        //닉네임 수정
        binding.btnNicknameFIx.setOnClickListener{
            val userNickname=binding.nickName.text.toString()
            val editAccountInfoDClass=EditAccountInfoDClass(userAge,userEmail, userGender, userHeight,
                userNickname, userProfileImg, userWeight)
            //val message="유저 이메일 : "+userEmail
            //val toast=Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()

            loginService.editNickname(header, userNumber,editAccountInfoDClass)
                .enqueue(object:Callback<EditMemberInfoResponse>{
                    //통신 성공시

                    override fun onResponse(call: Call<EditMemberInfoResponse>, response: Response<EditMemberInfoResponse>) {
                        Toast.makeText(this@EditAccountInform,"통신 성공입니다!!",Toast.LENGTH_LONG).show()

                        Log.d("정보수정",response.toString())
                        Log.d("정보수정2",response.body()?.nickname.toString())

                        binding.textNickname.text= response.body()?.nickname.toString()
                        DataContainer.userNickname=response.body()?.nickname.toString()
                        Log.d("정보수정3",DataContainer.userNickname.toString())

                        val builder = AlertDialog.Builder(this@EditAccountInform)
                        builder.setTitle("수정 완료").
                        setMessage("정보 수정이 완료되었습니다.")
                        builder.show()
                    }
                    //통신 실패시
                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("정보수정",t.localizedMessage)
                    }
                })
        }
    }
}