package com.example.art_run_android.running

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import com.example.art_run_android.DataContainer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : BaseActivity() {
    private val mapsFragment = MapsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_activity_main)
        Log.d(
            "로그인7",
            DataContainer.userAge.toString() + "살의 " + DataContainer.userNickname + "로그인 완료!"
        )
        setContentView(R.layout.running_activity_main)
        setListener()

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.mapView, mapsFragment)
        transaction.commit()

        mapsFragment.setMapInitializedListener(object : MapsFragment.MapInitializedListener {
            override fun onMapInitializedEvent() {
                mapsFragment.setMyLocationBtn(true)
            }
        })
    }


    private fun setListener() {
        val tabLayout: TabLayout = findViewById(R.id.tabbar)
        val startButton: FloatingActionButton = findViewById(R.id.startButton)

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
        val bottomSheetDialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialogFragment)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        var opt1 = 0
        var opt2 = 0
        var distance = 0.0

        val editText: TextInputEditText = bottomSheetView.findViewById(R.id.editText_bs)
        val textInputLayout : TextInputLayout = bottomSheetView.findViewById(R.id.tilBs)

        val segmentedButtonGroup = bottomSheetView.findViewById<SegmentedButtonGroup>(R.id.sbg1).apply {
            this.setOnPositionChangedListener {
                opt1 = it
            }
        }

        val segmentedButtonGroup2 = bottomSheetView.findViewById<SegmentedButtonGroup>(R.id.sbg2).apply {
            this.setOnPositionChangedListener {
                opt1 = it
                when(it) {
                    0 -> {
                        opt2 = 0
                        textInputLayout.suffixText = "m"
                        textInputLayout.hint = "거리..."
                    }
                    1 -> {
                        opt2 = 1
                        textInputLayout.suffixText = "min"
                        textInputLayout.hint = "시간..."
                    }
                    2 -> {
                        opt2 = 2
                        textInputLayout.suffixText = "kcal"
                        textInputLayout.hint = "칼로리..."
                    }
                }
            }
        }

        editText.doOnTextChanged { _, _, _, _ ->
            if (textInputLayout.isErrorEnabled) {
                textInputLayout.isErrorEnabled = false
            }
        }

        val okayButton: FloatingActionButton = bottomSheetView.findViewById(R.id.button_okay)

        okayButton.setOnClickListener {
            val inputNum = editText.text.toString().toIntOrNull()
            if (inputNum != null) {
                when (opt2) {
                    0 -> {
                        distance = inputNum.toDouble()
                    }
                    1 -> {
                        distance = if (opt1 == 0) {
                            inputNum * 4 / 60.0 * 1000
                        } else {
                            inputNum * 8 / 60.0 * 1000
                        }
                    }
                    2 -> {
                        distance = if (opt1 == 0) {
                            (inputNum * 200 * 4) / (2.9 * 3.5 * DataContainer.userWeight!! * 60) * 1000
                        } else {
                            (inputNum * 200 * 8) / (8.0 * 3.5 * DataContainer.userWeight!! * 60) * 1000
                        }
                    }
                }

                val intent = Intent(this, CourseRunActivity::class.java)
                intent.putExtra("distance", distance)
                intent.putExtra("speed", opt1)
                startActivity(intent)
            } else {
                textInputLayout.error = "숫자를 입력해주세요"
            }
        }
    }

}
