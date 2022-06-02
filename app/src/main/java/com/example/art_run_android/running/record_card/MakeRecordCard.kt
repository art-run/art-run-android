package com.example.art_run_android.running.record_card

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.running.ArtRunClient
import com.example.art_run_android.running.CompleteRoute
import com.example.art_run_android.running.RouteId
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputEditText
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class MakeRecordCard : AppCompatActivity(){

    private lateinit var map:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_record_card)

        val distance = intent.getIntExtra("distance",0)
        val kcal = intent.getIntExtra("kcal",0)
        val time = intent.getIntExtra("time",0)
        val finalRoutePrim = intent.getStringExtra("finalRoute")
        var lockStatus = true
        var lineColor = "black"
        var lineStyle = ""

        val title : TextInputEditText= findViewById(R.id.id_title_text)
        val completeBtn : Button = findViewById(R.id.save_card)
        val colorChange : ImageButton = findViewById(R.id.color_change)
        val lineChange : ImageButton = findViewById(R.id.line_change)
        val lockBtn : ImageButton = findViewById(R.id.lockMRC)
        val finalRoute = PolyUtil.decode(finalRoutePrim)
        val routeLocation = calculateCenterPosition(finalRoute)
        lateinit var polyline: Polyline

        val callback = OnMapReadyCallback { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routeLocation, 16f))
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            polyline = map.addPolyline(PolylineOptions().clickable(true).addAll(finalRoute))
        }

        val mapView: MapView = findViewById<MapView?>(R.id.FixedMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }
        // lock --> 다음 activity에서, 기록카드 에 공유 여부를 boolean 형태로 저장시켜서 넘기고 싶은데 구상을 못했습니다..! ㅜㅜ
        lockBtn.setOnClickListener {
            if(lockStatus){
                lockStatus = false
                lockBtn.setImageResource(R.drawable.ic_outline_lock_open_24)

            } else {
                lockStatus = true
                lockBtn.setImageResource(R.drawable.ic_outline_lock_24)
            }
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
                        polyline.pattern = listOf(Dash(20F), Gap(20F))
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_line ->{
                        Toast.makeText(applicationContext,"실선", Toast.LENGTH_SHORT).show()
                        polyline.pattern = null
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
                        polyline.color = Color.BLACK
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_white -> {
                        Toast.makeText(applicationContext,"하얀색", Toast.LENGTH_SHORT).show()
                        polyline.color = Color.WHITE
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_yello -> {
                        Toast.makeText(applicationContext,"노란색", Toast.LENGTH_SHORT).show()
                        polyline.color = Color.YELLOW
                        return@setOnMenuItemClickListener true
                    }
                    R.id.line_blue -> {
                        Toast.makeText(applicationContext,"파란색", Toast.LENGTH_SHORT).show()
                        polyline.color = Color.BLUE
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }


        // 저장 버튼 누르면 완료 화면으로 넘어간다
        completeBtn.setOnClickListener{
            if(title.text!=null) {
                var queryMap1 = mapOf( "color" to lineColor, "distance" to distance.toString(), "isPublic" to lockStatus.toString(),
                    "kcal" to kcal.toString(), "memberId" to DataContainer.userEmail, "routeId" to "0", "thickness" to lineStyle,
                    "time" to time.toString(), "title" to title.text.toString(), "wktRunRoute" to polylineToWkt(finalRoute))
                val callPostRouteFinish = ArtRunClient.routeApiService.postRouteFinish(
                    DataContainer.header, queryMap1)
                val intent = Intent(this, CompleteRecordCard::class.java)

                callPostRouteFinish.enqueue(object : Callback<RouteId> {
                    override fun onResponse(call: Call<RouteId>, response: Response<RouteId>) {
                        if (response.isSuccessful) { // <--> response.code == 200
                            Log.d("post route finish","통신 성공")
                            val routeId = response.body() as RouteId

                            //intent.putExtra("routeID", routeId.routeId)

                            intent.putExtra("routeID", routeId.routeId)  //제목 intent로 전달해주기


                        } else { // code == 400
                            Log.d("post route finish","통신 실패 : " + response.errorBody()?.string()!!)
                        }
                    }

                    override fun onFailure(call: Call<RouteId>, t: Throwable) {
                        Log.d("post route finish", "통신 실패 : $t")
                    }

                })

                intent.putExtra("title",title.text.toString())
                intent.putExtra("lineStyle",lineStyle)
                intent.putExtra("lineColor",lineColor)
                intent.putExtra("route",finalRoutePrim)
                startActivity(intent)

            }
        }
    }

    private fun calculateCenterPosition(polyline: MutableList<LatLng>): LatLng {
        val maxLat = polyline.maxOf { it.latitude }
        val minLat = polyline.minOf { it.latitude }
        val maxLng = polyline.maxOf { it.longitude }
        val minLng = polyline.minOf { it.longitude }

        return LatLng((maxLat + minLat) / 2, (maxLng + minLng) / 2)
    }

    private fun polylineToWkt(polyline: List<LatLng>): String {
        var sb= StringBuilder()
        sb.append("LINESTRING (")


        polyline.forEach{
            sb.append(it.longitude.toString())
            sb.append(" ")
            sb.append(it.latitude.toString())
            if(it != polyline.last()) {
                sb.append(", ")
            }
        }
        sb.append(")")

        return sb.toString()
    }
}