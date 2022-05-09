package com.example.art_run_android

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*

class MakeRecordCard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_record_card)
        val title : EditText= findViewById(R.id.id_title_text)
        val completeBtn : Button = findViewById(R.id.save_card)
        val colorChange : ImageButton = findViewById(R.id.color_change)
        val lineChange : ImageButton = findViewById(R.id.line_change)
        val lockBtn : ImageButton = findViewById(R.id.lockable)
        // lock --> 다음 activity에서, 기록카드 에 공유 여부를 boolean 형태로 저장시켜서 넘기고 싶은데 구상을 못했습니다..! ㅜㅜ
        lockBtn.setOnClickListener {
        // 기본 true-- lock/ false - unlock
        // if현재 상태 true 면 false .. ~~
        }

        // 선 굵기 변경 --> res 의 menu를 이용해 구성, 내용물은 아직..
        lineChange.setOnClickListener {
            var linemenu = PopupMenu(applicationContext,it)
            menuInflater?.inflate(R.menu.card_line,linemenu.menu)
            linemenu.show()
            linemenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.line_dot ->{
                        Toast.makeText(applicationContext,"점선", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_line ->{
                        Toast.makeText(applicationContext,"실선", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }

        //색상 버튼 --> res 의 menu를 사용해 구성, 색상 변경은 아직..
        colorChange.setOnClickListener {
           var colorPopup = PopupMenu(applicationContext,it)
            menuInflater?.inflate(R.menu.card_color,colorPopup.menu)
            colorPopup.show()
            colorPopup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.line_black -> {
                        Toast.makeText(applicationContext,"검은색", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_white -> {
                        Toast.makeText(applicationContext,"하얀색", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_yello -> {
                        Toast.makeText(applicationContext,"노란색", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_blue -> {
                        Toast.makeText(applicationContext,"파란색", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }


        // 저장 버튼 누르면 완료 화면으로 넘어간다
        completeBtn.setOnClickListener{
            val intent = Intent(this,CompleteRecordCard::class.java)
            intent.putExtra("title",title.toString())  //제목 intent로 전달해주기
            startActivity(intent)
        }


    }








}