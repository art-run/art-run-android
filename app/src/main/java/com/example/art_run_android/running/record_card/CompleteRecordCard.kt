package com.example.art_run_android.running.record_card

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteRecordCard : AppCompatActivity() {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compelete_record_card)
        val finishRouteId = intent.getIntExtra("finishRouteID",0)
        val lockButton: ImageButton = findViewById(R.id.lockCRC)
        val dotmenu : ImageButton = findViewById(R.id.shareCRC)
        val titleFixed: TextView = findViewById(R.id.titleCRC)
        val saveCardButton: Button = findViewById(R.id.save_card)
        lockButton.isVisible = false

        val callback = OnMapReadyCallback { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5662952, 126.97794509999994), 16f))
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            val callGetRoute = ArtRunClient.routeApiService.getRoute(
                DataContainer.header, finishRouteId)

            callGetRoute.enqueue(object : Callback<CompleteRoute> {
                override fun onResponse(call: Call<CompleteRoute>, response: Response<CompleteRoute>) {
                    if (response.isSuccessful) { // <--> response.code == 200
                        val completeRoute = response.body() as CompleteRoute
                        Log.d("get route","통신 성공 : ${completeRoute.title}")

                        val polylinePrim = wktToPolyline(completeRoute.wktRunRoute)
                        val centerLocation = calculateCenterPosition(polylinePrim)
                        map.moveCamera(CameraUpdateFactory.newLatLng(centerLocation))
                        val polyline = map.addPolyline(PolylineOptions().clickable(true).addAll(polylinePrim))
                        polyline.color = completeRoute.color.toInt()

                        titleFixed.text=completeRoute.title


                    } else { // code == 400
                        Log.d("get route","통신 실패 : " + response.errorBody()?.string()!!)
                    }
                }

                override fun onFailure(call: Call<CompleteRoute>, t: Throwable) {
                    Log.d("get route", "통신 실패 : $t")
                }

            })
        }

        val mapView: MapView = findViewById<MapView?>(R.id.recordCardMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }

        dotmenu.setOnClickListener {
            var dotPopup = PopupMenu(applicationContext,it)
            menuInflater?.inflate(R.menu.card_dotmenu,dotPopup.menu)
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
                        Toast.makeText(applicationContext,"수정하기", Toast.LENGTH_SHORT).show()
                        return@setOnMenuItemClickListener true
                    }
                    else ->{ return@setOnMenuItemClickListener false}
                }
            }
        }

        saveCardButton.setOnClickListener {
            val intent = Intent(this, SocialActivity::class.java)
            startActivity(intent)
        }

    }

    // 뒤로 가기 하면 수정으로 돌아가는 것을 막기위해 뒤로가기 금지
    // *** 버튼으로 메뉴 클릭 시 공유/ 수정하기로 넘어갈 수 있다..
    override fun onBackPressed(){
        //
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
}