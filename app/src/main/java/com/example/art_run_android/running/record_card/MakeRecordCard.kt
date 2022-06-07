package com.example.art_run_android.running.record_card

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.running.ArtRunClient
import com.example.art_run_android.running.RouteFinishRequest
import com.example.art_run_android.running.RouteId
import com.github.antonpopoff.colorwheel.ColorWheel
import com.github.antonpopoff.colorwheel.gradientseekbar.GradientSeekBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MakeRecordCard : AppCompatActivity() {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_card_activity_make)

        val distance = intent.getIntExtra("distance", 0)
        val kcal = intent.getIntExtra("kcal", 0)
        val time = intent.getIntExtra("time", 0)
        val finalRoutePrim = intent.getStringExtra("finalRoute")
        var startRouteId = intent.getIntExtra("startRouteId", 0)
        var lockStatus = true
        var lineColor = Color.BLACK.toString()
        var lineStyle = "line"

        val title: TextInputEditText = findViewById(R.id.id_title_text)
        val titleLayout : TextInputLayout = findViewById(R.id.mrcTitle)
        val mrcDist: TextView = findViewById(R.id.mrcDist)
        val mrcKcal: TextView = findViewById(R.id.mrcKcal)
        val mrcSpeed: TextView = findViewById(R.id.mrcSpeed)
        val mrcTime: TextView = findViewById(R.id.mrcTime)

        val completeBtn: Button = findViewById(R.id.save_card)
        val colorChange: ImageButton = findViewById(R.id.color_change)
        val lineChange: ImageButton = findViewById(R.id.line_change)
        val lockBtn: ImageButton = findViewById(R.id.lockMRC)
        val finalRoute = PolyUtil.decode(finalRoutePrim)
        val routeLocation = calculateCenterPosition(finalRoute)
        lateinit var polyline: Polyline

        mrcDist.text = intent.getIntExtra("distance", 0).toString() + " m"
        mrcSpeed.text = intent.getIntExtra("speed", 0).toString() + " km/h"
        mrcTime.text = formatTime(intent.getIntExtra("time", 0))
        mrcKcal.text = intent.getIntExtra("kcal",0).toString() + " kcal"

        val callback = OnMapReadyCallback { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routeLocation, 16f))
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            polyline = map.addPolyline(PolylineOptions().clickable(true).addAll(finalRoute))
            if (intent.hasExtra("fixRouteId")) {
                val fixRouteId = intent.getIntExtra("fixRouteId", 0)
                title.setText(intent.getStringExtra("title"))
                if (intent.hasExtra("lineColor")) {
                    polyline.color = intent.getStringExtra("lineColor")!!.toInt()
                }
                startRouteId = fixRouteId
            }
        }

        val mapView: MapView = findViewById<MapView?>(R.id.FixedMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }

        lockBtn.setOnClickListener {
            if (lockStatus) {
                lockStatus = false
                lockBtn.setBackgroundResource(R.drawable.ic_outline_lock_open_24)

            } else {
                lockStatus = true
                lockBtn.setBackgroundResource(R.drawable.ic_outline_lock_24)
            }
        }

        lineChange.setOnClickListener { view ->
            var linemenu = PopupMenu(applicationContext, view)
            menuInflater?.inflate(R.menu.card_line, linemenu.menu)
            linemenu.show()
            linemenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.line_dot -> {
                        Toast.makeText(applicationContext, "점선", Toast.LENGTH_SHORT).show()
                        polyline.pattern = listOf(Dash(20F), Gap(20F))
                        lineStyle = "dot"
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_line -> {
                        Toast.makeText(applicationContext, "실선", Toast.LENGTH_SHORT).show()
                        polyline.pattern = null
                        lineStyle = "line"
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
        }

        title.doOnTextChanged { _, _, _, _ ->
            if(titleLayout.isErrorEnabled){
                titleLayout.isErrorEnabled = false
            }
        }

        val linearLayout:LinearLayout=findViewById(R.id.linearLayout6)

        colorChange.setOnClickListener {
            val popUpView = layoutInflater.inflate(R.layout.record_card_popupmenu,null)
            val colorPopUp = PopupWindow(popUpView,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            colorPopUp.isFocusable = true
            val intArray = IntArray(2)
            linearLayout.getLocationInWindow(intArray)
            val height = linearLayout.measuredHeight
            val width = linearLayout.measuredWidth

            colorPopUp.elevation = 20F
            colorPopUp.animationStyle = R.style.Animation
            colorPopUp.showAtLocation(colorChange,Gravity.NO_GRAVITY,intArray[0] + width/2 ,intArray[1] - height/4*3)

            val colorWheel : ColorWheel = popUpView.findViewById(R.id.colorWheel)
            val gradientSeekBar : GradientSeekBar = popUpView.findViewById(R.id.gradientSeekBar)

            colorWheel.colorChangeListener = { rgb ->
                gradientSeekBar.startColor = rgb
                val argb = gradientSeekBar.argb
                colorChange.backgroundTintList = ColorStateList.valueOf(argb)
                polyline.color = argb
                lineColor = argb.toString()
            }

            gradientSeekBar.colorChangeListener = { _: Float, argb: Int ->
                colorChange.backgroundTintList = ColorStateList.valueOf(argb)
                polyline.color = argb
                lineColor = argb.toString()
            }

            colorPopUp.setOnDismissListener {
                colorChange.backgroundTintList = null
            }
        }

        // 저장 버튼 누르면 완료 화면으로 넘어간다
        completeBtn.setOnClickListener {
            if (!title.text.isNullOrBlank()) {
                val intent = Intent(this, CompleteRecordCard::class.java)

                val requestBody = RouteFinishRequest(
                    lineColor,
                    distance,
                    (!lockStatus).toInt(),
                    kcal,
                    DataContainer.userNumber,
                    startRouteId,
                    "3",
                    time,
                    title.text.toString(),
                    polylineToWkt(finalRoute)
                )
                val callPostRouteFinish = ArtRunClient.routeApiService.postRouteFinish(
                    DataContainer.header, requestBody
                )

                callPostRouteFinish.enqueue(object : Callback<RouteId> {
                    override fun onResponse(call: Call<RouteId>, response: Response<RouteId>) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            val finishRouteId = response.body() as RouteId
                            Log.d("post route finish", "통신 성공 : ${finishRouteId.routeId}")

                            intent.putExtra("finishRouteID", finishRouteId.routeId)
                            startActivity(intent)

                        } else { // code == 400
                            Log.d(
                                "post route finish",
                                "통신 실패 : " + response.errorBody()?.string()!!
                            )
                            Toast.makeText(
                                applicationContext,
                                "서버와 연결하는 데 실패했습니다.\n다시 시도해 주세요.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RouteId>, t: Throwable) {
                        Log.d("post route finish", "통신 실패 : $t")
                        Toast.makeText(
                            applicationContext,
                            "서버와 연결하는 데 실패했습니다.\n다시 시도해 주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            } else {
                titleLayout.error = "제목을 입력해 주세요"
                Toast.makeText(applicationContext, "제목을 입력해 주세요", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun Boolean.toInt() = if (this) 1 else 0

    private fun calculateCenterPosition(polyline: MutableList<LatLng>): LatLng {
        val maxLat = polyline.maxOf { it.latitude }
        val minLat = polyline.minOf { it.latitude }
        val maxLng = polyline.maxOf { it.longitude }
        val minLng = polyline.minOf { it.longitude }

        return LatLng((maxLat + minLat) / 2, (maxLng + minLng) / 2)
    }

    private fun polylineToWkt(polyline: List<LatLng>): String {
        var sb = StringBuilder()
        sb.append("LINESTRING (")

        for (i in polyline.indices) {
            sb.append(polyline[i].longitude.toString())
            sb.append(" ")
            sb.append(polyline[i].latitude.toString())
            if (i != polyline.size - 1) {
                sb.append(", ")
            }
        }
        sb.append(")")

        return sb.toString()
    }

    private fun formatTime(time: Int): String {
        val hours = String.format("%02d", time / 3600)
        val minutes = String.format("%02d", (time % 3600) / 60)
        val seconds = String.format("%02d", ((time % 3600) % 60))

        return "$hours:$minutes:$seconds"
    }
}