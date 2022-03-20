package com.example.art_run_android.member_management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.art_run_android.R
import com.example.art_run_android.databinding.MemberManagementActivitySignUp2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MemberManagementActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //SignUpActivity에서 정보를 인텐트로 받아와 닉네임 칸을 수정한 후, 토스트 메시지로 다 받아왔는지 확인
        binding.textView9.text=intent.getStringExtra("nickname")
        var nickname=intent.getStringExtra("nickname").toString()
        var email=intent.getStringExtra("email").toString()
        var password=intent.getStringExtra("password").toString()

        val message="유저 이메일 : "+email+"\n유저 비밀번호 : "+ password+"\n유저 닉네임 : "+nickname
        val toast= Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.show()


        //뒤로가기 버튼 설정_ 눌렀을 때 SignUpActivity 로 돌아감.
        binding.btBack.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //라디오버튼 설정_성별 선택시 유저 정보 반영

        var gender:String="A"

        binding.radioGroup.setOnCheckedChangeListener{radioGroup, checkedGender->
            when(checkedGender){
                R.id.rbt_male -> {
                    gender="MALE"
                }
                R.id.rbt_female -> {
                    gender="FEMALE"
                }

            }

            //라디오버튼 클릭 리스너 즉시즉시 잘 작동하는지 확인
            val toast=Toast.makeText(this,"성별 : $gender",Toast.LENGTH_SHORT)
            toast.show()
        }


        //레트로핏생성
        var retrofit=Retrofit.Builder()
            .baseUrl("http://artrun.kro.kr:80")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            //인터페이스(로그인서비스) 생성
        var loginService=retrofit.create(LoginService::class.java)



        //확인 버튼 설정_ 눌렀을 때 받아온 정보를 서버로 넘김.
        binding.btConfirm.setOnClickListener{
            var height=binding.textHeight.text.toString().toInt()
            var weight=binding.textWeight.text.toString().toInt()
            var age=binding.textWeight.text.toString().toInt()

            //val toast = Toast.makeText(this,"나이 : $age, email: $email, 성별 : $gender, 키 : $height, 닉네임 : $nickname, 비밀번호 : $password,  몸무게 : $weight, ",Toast.LENGTH_SHORT)
            //toast.show()

            //여태 받은 자료들을 Json 객체로 생성
            val memberInfoDClass=MemberInfoDClass(age,email,gender,height,nickname,password,weight)

            loginService.signUp(memberInfoDClass)
                .enqueue(object: Callback<SignUpResponse>{
                    //통신 실패시 실행되는 코드
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        //Log.e("회원가입", t.message.toString())
                        Log.e("회원가입", "${t.localizedMessage}")

                        Toast.makeText(this@SignUpActivity2,"통신 실패!",Toast.LENGTH_LONG).show()
                    }

                    //통신 성공시 실행되는 코드
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                        //Log.d("SignUp",signupCheck)
                        //val signupCheck=response.body()?.email.toString()
                        var signUpCheck=response
                        Log.d("회원가입",signUpCheck.toString())
                        Log.d("회원가입2",signUpCheck.body().toString())
                        val intent=Intent(this@SignUpActivity2, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                )


        }



    }
}