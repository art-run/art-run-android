package com.example.art_run_android

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast

class ShareRecordCard1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_record_card1)
        val showmap: Switch = findViewById(R.id.share_show_map)
        val showstats : Switch = findViewById(R.id.share_show_statistic)
        val back : ImageButton = findViewById(R.id.share_back)
        val goto_share: ImageButton = findViewById(R.id.share_select_SNS)

        // 이미지 공유 기능 ( 찾는 중.. )
        goto_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val chooser = Intent.createChooser(intent, "공유하기")
            startActivity(chooser)  // 공유하는 창 뜨는거

            }
        // 뒤로 돌아간다 --> 공유하기 액티비티를 finish
        back.setOnClickListener {
            finish()
        }
        // 지도 표시 스위치, 기본으로 표시를 한다
        showmap.setChecked(true)
        showmap.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"지도 표시",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"지도 표시 해제",Toast.LENGTH_SHORT).show()
            }
        }

        // 통계 표시 스위치 , 기본으로 표시 해놓는다.
        showstats.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"통계 표시",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"통계 표시 해제",Toast.LENGTH_SHORT).show()
            }
        }



    }
}