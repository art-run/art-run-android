package com.example.art_run_android

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)

    private val REQUEST_PERMISSION_CODE = 1

    private val DEFAULT_ZOOM_LEVEL = 17f

    private val CITY_HALL = LatLng(37.5662952, 126.97794509999994)

    private var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
        mapView.onCreate(savedInstanceState)

        if (checkPermissions()) {
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
        }
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
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_setcourse, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        val textView : TextView = bottomSheetView.findViewById(R.id.textView_bs)

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
                when(index){
                    0-> textView.text = "km"
                    1-> textView.text = "분"
                    2-> textView.text = "kcal"
                }
            }
        }

        val okayButton: Button = bottomSheetView.findViewById(R.id.button_okay)

        okayButton.setOnClickListener {
            val intent = Intent(this, CourseRunActivity::class.java)
            startActivity(intent)
        }

    }

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
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM_LEVEL))
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
            CITY_HALL
        }
    }

}
