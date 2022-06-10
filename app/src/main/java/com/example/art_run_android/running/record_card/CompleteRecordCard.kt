package com.example.art_run_android.running.record_card

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.SocialActivity
import com.example.art_run_android.running.ArtRunClient
import com.example.art_run_android.running.CompleteRoute
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.running_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE
import kotlin.math.roundToInt

class CompleteRecordCard : AppCompatActivity() {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_card_activity_compelete)
        val finishRouteId = intent.getIntExtra("finishRouteID",0)
        val lockButton: ImageButton = findViewById(R.id.lockCRC)
        val dotmenu : ImageButton = findViewById(R.id.shareCRC)
        val titleFixed: TextView = findViewById(R.id.titleCRC)
        val saveCardButton: Button = findViewById(R.id.save_card)

        val crcDist: TextView = findViewById(R.id.crcDist)
        val crcKcal: TextView = findViewById(R.id.crcKcal)
        val crcSpeed: TextView = findViewById(R.id.crcSpeed)
        val crcTime: TextView = findViewById(R.id.crcTime)
        lateinit var polyline: Polyline
        lateinit var completeRoute: CompleteRoute
        lockButton.isVisible = false

        if(intent.hasExtra("public")){
            dotmenu.isVisible = false
        }

        val callback = OnMapReadyCallback { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5662952, 126.97794509999994), 16f))

            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false

            try {
                val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style2))
                if (!success) {
                    Log.e("loading Style", "Style parsing failed.");
                }
            } catch (e : Resources.NotFoundException) {
                Log.e("loading Style", "Can't find style. Error: ", e);
            }

            val callGetRoute = ArtRunClient.routeApiService.getRoute(
                DataContainer.header, finishRouteId)

            callGetRoute.enqueue(object : Callback<CompleteRoute> {
                override fun onResponse(call: Call<CompleteRoute>, response: Response<CompleteRoute>) {
                    if (response.isSuccessful) { // <--> response.code == 200
                        completeRoute = response.body() as CompleteRoute
                        Log.d("get route","통신 성공 : ${completeRoute.title}")

                        val polylinePrim = wktToPolyline(completeRoute.wktRunRoute)
                        val centerLocation = calculateCenterPosition(polylinePrim)
                        map.moveCamera(CameraUpdateFactory.newLatLng(centerLocation))
                        polyline = map.addPolyline(PolylineOptions().clickable(true).addAll(polylinePrim))
                        polyline.color = completeRoute.color.toInt()

                        titleFixed.text=completeRoute.title
                        crcDist.text = "${completeRoute.distance} m"
                        crcSpeed.text = "${completeRoute.speed.roundToInt()} km/h"
                        crcTime.text = formatTime(completeRoute.time)
                        crcKcal.text = "${completeRoute.kcal} kcal"

                    } else { // code == 400
                        Log.d("get route","통신 실패 : " + response.errorBody()?.string()!!)
                        mapView.isVisible = false
                        Toast.makeText(applicationContext,"데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CompleteRoute>, t: Throwable) {
                    Log.d("get route", "통신 실패 : $t")
                    mapView.isVisible = false
                    Toast.makeText(applicationContext,"데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

            })
        }

        val mapView: MapView = findViewById<MapView?>(R.id.recordCardMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }

        dotmenu.setOnClickListener {
            val dotPopup = PopupMenu(applicationContext,it)
            menuInflater.inflate(R.menu.card_dotmenu,dotPopup.menu)
            dotPopup.show()
            dotPopup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.card_go_to_share ->{
                        // 공유하기 버튼을 누르면 공유하기 activity로 넘어간다
                        Toast.makeText(applicationContext,"공유하기", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ShareRecordCard1::class.java)
                        intent.putExtra("finishRouteId",finishRouteId)
                        startActivity(intent)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.card_back_to_make ->{
                        // 다시 돌아가는 건 아직..!
                        val intent = Intent(this,MakeRecordCard::class.java)
                        val finalRoutePrim = PolyUtil.encode(polyline.points)
                        intent.putExtra("finalRoute",finalRoutePrim)
                        intent.putExtra("distance",completeRoute.distance)
                        intent.putExtra("kcal",completeRoute.kcal)
                        intent.putExtra("time", completeRoute.time)
                        intent.putExtra("fixRouteId",finishRouteId)
                        intent.putExtra("lineColor",completeRoute.color)
                        intent.putExtra("title",completeRoute.title)
                        intent.putExtra("isPublic",completeRoute.isPublic)
                        startActivity(intent)
                        Toast.makeText(applicationContext,"수정하기", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.card_delete -> {
                        val alertDialogBuilder = AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("기록카드 삭제")
                        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                        alertDialogBuilder.setNegativeButton("예"
                        ) { _: DialogInterface, _: Int ->
                            callDelete(finishRouteId)
                        }
                        alertDialogBuilder.setPositiveButton("아니오"
                        ) { _: DialogInterface, _: Int ->
                            return@setPositiveButton
                        }
                        alertDialogBuilder.show()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }

        saveCardButton.setOnClickListener {
            onBackPressed()
        }

    }

    // 뒤로 가기 하면 수정으로 돌아가는 것을 막기위해 뒤로가기 금지
    // *** 버튼으로 메뉴 클릭 시 공유/ 수정하기로 넘어갈 수 있다..
    override fun onBackPressed(){
        val intent = Intent(this, SocialActivity::class.java)
        startActivity(intent)
    }

    private fun calculateCenterPosition(polyline: MutableList<LatLng>): LatLng {
        val maxLat = polyline.maxOf { it.latitude }
        val minLat = polyline.minOf { it.latitude }
        val maxLng = polyline.maxOf { it.longitude }
        val minLng = polyline.minOf { it.longitude }

        return LatLng((maxLat + minLat) / 2, (maxLng + minLng) / 2)
    }

    private fun wktToPolyline(wktRoute: String): MutableList<LatLng> {
        val prim = wktRoute.substring(wktRoute.indexOf('(') + 1, wktRoute.indexOf(')')).split(", ")
        val polyline = mutableListOf<LatLng>().apply {
            prim.forEach {
                val prim2 = it.split(" ")
                if (prim2.size >= 2) {
                    this.add(LatLng(prim2[1].toDouble(), prim2[0].toDouble()))
                }
            }
        }
        return polyline
    }

    private fun formatTime(time : Int): String {
        val hours = String.format("%02d", time/3600)
        val minutes = String.format("%02d",(time%3600)/60)
        val seconds = String.format("%02d",((time%3600)%60))

        return "$hours:$minutes:$seconds"
    }

    private fun callDelete(routeId : Int){
        val callDeleteRoute = ArtRunClient.routeApiService.deleteRoute(DataContainer.header,routeId)
        callDeleteRoute.enqueue(object : Callback<DELETE> {
            override fun onResponse(call: Call<DELETE>, response: Response<DELETE>) {
                if (response.isSuccessful) { // <--> response.code == 200
                    Log.d("delete route","통신 성공")
                    Toast.makeText(applicationContext,"기록을 삭제했습니다.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@CompleteRecordCard, SocialActivity::class.java)
                    finishAffinity()
                    startActivity(intent)

                } else { // code == 400
                    Log.d("delete route","통신 실패 : " + response.errorBody()?.string()!!)
                    Toast.makeText(applicationContext,"삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DELETE>, t: Throwable) {
                Log.d("delete route", "통신 실패 : $t")
                Toast.makeText(applicationContext,"삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}