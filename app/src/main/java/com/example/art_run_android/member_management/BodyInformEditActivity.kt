package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.art_run_android.DataContainer
import com.example.art_run_android.DataContainer.Companion.header
import com.example.art_run_android.DataContainer.Companion.userEmail
import com.example.art_run_android.DataContainer.Companion.userGender
import com.example.art_run_android.DataContainer.Companion.userNickname
import com.example.art_run_android.DataContainer.Companion.userNumber
import com.example.art_run_android.DataContainer.Companion.userProfileImg
import com.example.art_run_android.R
import com.example.art_run_android.databinding.MemberManagementActivityBodyInformEditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BodyInformEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=MemberManagementActivityBodyInformEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //멤버 아이디 임시로 초기화
        var memberId=1


        //뒤로가기를 누르면
        binding.btBack.setOnClickListener{
            val intent = Intent(this, SelectSettingsActivity::class.java)
            startActivity(intent)
        }
        //닉네임과 프로필 불러오기
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

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedGender ->
            when (checkedGender) {
                R.id.rabtnMale -> {
                    DataContainer.userGender= "MALE"
                    Log.d("성별 선택", "${DataContainer.userGender}")
                }
                R.id.rabtnFemale -> {
                    DataContainer.userGender = "FEMALE"
                    Log.d("성별 선택", "${DataContainer.userGender}")                }

            }
        }
        binding.btConfirm.setOnClickListener {
            val height = binding.textHeight.text.toString().toInt()
            val weight = binding.textWeight.text.toString().toInt()
            val age = binding.textAge.text.toString().toInt()
            //성별 체크먼저

            val editAccountInfoDClass = EditAccountInfoDClass(age, userEmail, userGender,height,
                userNickname, userProfileImg,weight)
            loginService.editBodyInfo(header,userNumber.toString(),editAccountInfoDClass)
                .enqueue(object : Callback<EditMemberInfoResponse> {
                    //통신 실패시 실행되는 코드
                    override fun onFailure(call: Call<EditMemberInfoResponse>, t: Throwable) {
                        Log.e("정보수정", "${t.localizedMessage}")

                      }

                    //통신 성공시 실행되는 코드
                    override fun onResponse(call: Call<EditMemberInfoResponse>, response: Response<EditMemberInfoResponse>) {
                        //Log.d("SignUp",signupCheck)
                        //val signupCheck=response.body()?.email.toString()
                        Log.d("정보수정",response.toString())
                        Log.d("정보수정2",response.body().toString())
                        Log.d("정보수정3",editAccountInfoDClass.toString())

                        }
                    }

                )
            //val message = "키 : "+height+"cm\n몸무게 : "+weight+"kg\n나이 : "+age

            //val toast = Toast.makeText(this,message,Toast.LENGTH_LONG)
            //toast.show()
            //val intent = Intent(this, SelectSettingsActivity::class.java)
            //startActivity(intent)
            }
        }
    }
