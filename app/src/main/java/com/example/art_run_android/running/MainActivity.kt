package com.example.art_run_android.running

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.art_run_android.BaseActivity
import com.example.art_run_android.R
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout


class MainActivity : BaseActivity() {
    /*
    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)

    val REQUEST_PERMISSION_CODE = 1

    val DEFAULT_ZOOM_LEVEL = 17f

    val defaultLocation = LatLng(37.5662952, 126.97794509999994)

    var googleMap: GoogleMap? = null
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //로그인때 받아온 유저 정보를 저장함.
        var userAge=intent.getStringExtra("Age")?.toInt()
        var userEmail=intent.getStringExtra("Email").toString()
        var userGender=intent.getStringExtra("Gender").toString()
        var userHeight=intent.getStringExtra("Height")?.toInt()
        var userNickname=intent.getStringExtra("Nickname").toString()
        var userPassword=intent.getStringExtra("Password").toString()
        var userWeight=intent.getStringExtra("Weight")?.toInt()
        Log.d("로그인7",userAge.toString()+"살의 "+userNickname+"로그인 완료!")


        setContentView(R.layout.running_activity_main)
        setListener()

        val transaction = supportFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()

        transaction.add(R.id.mapView, mapsFragment)
        transaction.commit()
        /*
            mapView.onCreate(savedInstanceState)

            if (checkPermissions()) {
                initMap()
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
            }
             */
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

        val textView: TextView = bottomSheetView.findViewById(R.id.textView_bs)

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
                for (ton in opt2ButtonList) {
                    if (but == ton) {
                        continue;
                    }
                    ton.setBackgroundColor(Color.WHITE)
                }
                val index = opt2ButtonList.indexOf(but)
                when (index) {
                    0 -> textView.text = "km"
                    1 -> textView.text = "분"
                    2 -> textView.text = "kcal"
                }
            }
        }

        val okayButton: Button = bottomSheetView.findViewById(R.id.button_okay)

        okayButton.setOnClickListener {
            val intent = Intent(this, CourseRunActivity::class.java)
            startActivity(intent)
        }

    }
/*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        initMap()
    }

    private fun checkPermissions(): Boolean {

        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun initMap() {
        mapView.getMapAsync {

            googleMap = it
            it.uiSettings.isMyLocationButtonEnabled = false

            when {
                checkPermissions() -> {
                    it.isMyLocationEnabled = true
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL))
                }
                else -> {
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM_LEVEL))
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getMyLocation(): LatLng {

        val locationProvider: String = LocationManager.GPS_PROVIDER

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val lastKnownLocation: Location? = locationManager.getLastKnownLocation(locationProvider)

        return if (lastKnownLocation != null) {
            LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
        } else {
            defaultLocation
        }
    }
*/
}
