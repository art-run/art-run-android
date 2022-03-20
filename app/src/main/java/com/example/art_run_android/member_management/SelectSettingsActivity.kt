package com.example.art_run_android.member_management

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R

class SelectSettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_management_activity_select_settings)

        val button6: Button = findViewById(R.id.bt_bodyInfoEdit)
        val button7: Button = findViewById(R.id.bt_editAccountInfo)
        val button8: Button = findViewById(R.id.bt_runningSetting)
        button6.setOnClickListener {
            startActivity(Intent(this, BodyInformEditActivity::class.java))
        }
        button7.setOnClickListener {
            startActivity(Intent(this, EditAccountInform::class.java))
        }
        button8.setOnClickListener {
            startActivity(Intent(this, RunningSettingActivity::class.java))
        }
    }
}