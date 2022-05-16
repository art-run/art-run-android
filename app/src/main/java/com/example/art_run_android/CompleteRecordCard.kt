package com.example.art_run_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_compelete_record_card.*

class CompleteRecordCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compelete_record_card)
        val lockButton: ImageButton = findViewById(R.id.btn_lock_c)
        val unlockButton: ImageButton = findViewById(R.id.btn_unlock_c)
        val dotmenu : ImageButton = findViewById(R.id.share_btn)
        // 앞의 activity 에서 lock 의 상태를 받아 complete에서는 변경시키지 못하고, 수정으로 넘어가야만 할 수 있는 것..!
        // lock의 상태에 따라 모양만 변경시키게 하려는 의도.. --> 변경하는게 나을까요..?
        lockButton.isVisible = false
        unlockButton.isVisible = false
        // dot 메뉴로 수정하기(뒤로 이동) , share activity로 이동
        dotmenu.setOnClickListener {
            var dotPopup = PopupMenu(applicationContext,it)
            menuInflater?.inflate(R.menu.card_dotmenu,dotPopup.menu)
            dotPopup.show()
            dotPopup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.card_go_to_share ->{
                        // 공유하기 버튼을 누르면 공유하기 activity로 넘어간다
                        Toast.makeText(applicationContext,"공유하기", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,ShareRecordCard1::class.java)
                        intent.putExtra("title",title.toString())  //제목 intent로 전달해주기
                        startActivity(intent)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.card_back_to_make ->{
                        // 다시 돌아가는 건 아직..!
                        Toast.makeText(applicationContext,"수정하기", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }

        // 제목 받아서 오기
        if(intent.hasExtra("title")){  // 전단계에서 intent로 title을 받아 title 설정해주기
            title_fixed.text=intent.getStringExtra("title")
        }
    }

    // 뒤로 가기 하면 수정으로 돌아가는 것을 막기위해 뒤로가기 금지
    // *** 버튼으로 메뉴 클릭 시 공유/ 수정하기로 넘어갈 수 있다..
    override fun onBackPressed(){
        //
    }
}