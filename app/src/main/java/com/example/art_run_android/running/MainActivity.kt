package com.example.art_run_android.running

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import com.example.art_run_android.DataContainer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout


class MainActivity : BaseActivity() {
    private val mapsFragment = MapsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_main)
        Log.d("로그인7", DataContainer.userAge.toString()+"살의 "+DataContainer.userNickname+"로그인 완료!")
        setContentView(R.layout.running_activity_main)
        setListener()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.mapView, mapsFragment)
        transaction.commit()
    }


    private fun setListener() {
        val tabLayout: TabLayout = findViewById(R.id.tabbar)
        val startButton: ImageButton = findViewById(R.id.startButton)

        tabLayout.selectTab(tabLayout.getTabAt(0))

        startButton.setOnClickListener {
            if (tabLayout.selectedTabPosition == 0) {

                val intent = Intent(this, FreeRunActivity::class.java)
                startActivity(intent)
            }
            if (tabLayout.selectedTabPosition == 1) {
                setCourse()
            }
        }
    }

    private fun setCourse() {
        val bottomSheetView = layoutInflater.inflate(R.layout.running_bottomsheet_setcourse, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        var opt1 = -1
        var opt2 = -1
        var distance = 0.0

        val textView: TextView = bottomSheetView.findViewById(R.id.textView_bs)
        val editText: EditText = bottomSheetView.findViewById(R.id.editText_bs)

        val opt1ButtonList: List<Button> by lazy {
            listOf<Button>(
                bottomSheetView.findViewById(R.id.button_opt1_walk),
                bottomSheetView.findViewById(R.id.button_opt1_run)
            )
        }

        val opt2ButtonList: List<Button> by lazy {
            listOf<Button>(
                bottomSheetView.findViewById(R.id.button_opt2_dist),
                bottomSheetView.findViewById(R.id.button_opt2_time),
                bottomSheetView.findViewById(R.id.button_opt2_kcal)
            )
        }

        for (but in opt1ButtonList) {
            but.setOnClickListener {
                but.setBackgroundColor(Color.GRAY)
                opt1 = opt1ButtonList.indexOf(but)
                for (ton in opt1ButtonList) {
                    if (but == ton) {
                        continue;
                    }
                    ton.setBackgroundColor(Color.WHITE)
                }
            }
        }

        for (but in opt2ButtonList) {
            but.setOnClickListener {
                but.setBackgroundColor(Color.GRAY)
                opt2 = opt2ButtonList.indexOf(but)
                for (ton in opt2ButtonList) {
                    if (but == ton) {
                        continue;
                    }
                    ton.setBackgroundColor(Color.WHITE)
                }
                when (opt2) {
                    0 -> textView.text = "km"
                    1 -> textView.text = "분"
                    2 -> textView.text = "kcal"
                }
            }
        }

        val okayButton: Button = bottomSheetView.findViewById(R.id.button_okay)

        okayButton.setOnClickListener {
            val inputNum = editText.text.toString().toIntOrNull()
            if(opt1 != -1 && opt2 != -1 && inputNum != null) {
                when(opt2) {
                    0 -> {
                        distance = inputNum.toDouble()
                    }
                    1 -> {
                        distance = if(opt1 == 0) {
                            inputNum*4/60.0
                        } else {
                            inputNum*8/60.0
                        }
                    }
                    2 -> {
                        distance = if(opt1 == 0) {
                            (inputNum*20*4)/(2.9*3.5*DataContainer.userWeight!!*60)
                        } else {
                            (inputNum*20*8)/(8.0*3.5*DataContainer.userWeight!!*60)
                        }
                    }
                }

                val intent = Intent(this, CourseRunActivity::class.java)
                intent.putExtra("distance", distance)
                intent.putExtra("currentLocation", mapsFragment.currentLocation)
                startActivity(intent)
            } else {
                //다시입력
            }
        }
    }

}
