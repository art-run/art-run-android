package com.example.art_run_android.running.record_card

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.art_run_android.DataContainer
import com.example.art_run_android.R
import com.example.art_run_android.running.ArtRunClient
import com.example.art_run_android.running.CompleteRoute
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.running_activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt


class ShareRecordCard1 : AppCompatActivity() {
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_card_activity_share)
        val showmap: Switch = findViewById(R.id.share_show_map)
        val showstats : Switch = findViewById(R.id.share_show_statistic)
        val back : ImageButton = findViewById(R.id.share_back)
        val goto_share: ImageButton = findViewById(R.id.share_select_SNS)
        val frameLayout : FrameLayout = findViewById(R.id.ShareView)
        val linearLayout : LinearLayout = findViewById(R.id.linearLayout8)

        val shareTitle : TextView = findViewById(R.id.shareTitle)
        val srcDist: TextView = findViewById(R.id.srcDist)
        val srcSpeed: TextView = findViewById(R.id.srcSpeed)
        val srcTime: TextView = findViewById(R.id.srcTime)

        val finishRouteId = intent.getIntExtra("finishRouteId",0)

        lateinit var bm:Bitmap

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
                        shareTitle.text = completeRoute.title
                        srcDist.text = "${completeRoute.distance} m"
                        srcSpeed.text = "${completeRoute.speed.roundToInt()} km/h"
                        srcTime.text = formatTime(completeRoute.time)

                    } else { // code == 400
                        Log.d("get route","통신 실패 : " + response.errorBody()?.string()!!)
                        frameLayout.isVisible = false
                        Toast.makeText(applicationContext,"데이터를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CompleteRoute>, t: Throwable) {
                    Log.d("get route", "통신 실패 : $t")
                    frameLayout.isVisible = false
                    Toast.makeText(applicationContext,"데이터를 불러올 수 없습니다.", Toast.LENGTH_LONG).show()
                }

            })
        }

        val mapView: MapView = findViewById<MapView?>(R.id.ShareMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }

        // 이미지 공유 기능 ( 찾는 중.. )
        goto_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            bm = getBitmapFromView(frameLayout)
            val uri: Uri? = saveImage(bm)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            val chooser = Intent.createChooser(intent, "공유하기")
            startActivity(chooser)  // 공유하는 창 뜨는거

            }
        // 뒤로 돌아간다 --> 공유하기 액티비티를 finish
        back.setOnClickListener {
            finish()
        }
        // 지도 표시 스위치, 기본으로 표시를 한다
        showmap.isChecked = true
        showmap.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"지도 표시",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"지도 표시 해제",Toast.LENGTH_SHORT).show()
            }
        }

        // 통계 표시 스위치 , 기본으로 표시 해놓는다.
        showstats.isChecked = true
        showstats.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"통계 표시",Toast.LENGTH_SHORT).show()
                linearLayout.isVisible = true
            }
            else{
                Toast.makeText(applicationContext,"통계 표시 해제",Toast.LENGTH_SHORT).show()
                linearLayout.isVisible = false
            }
        }
    }

    private fun getBitmapFromView(frameLayout: FrameLayout): Bitmap {
        var bitmap =
            Bitmap.createBitmap(frameLayout.width, frameLayout.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        frameLayout.draw(canvas)
        return bitmap
    }

    private fun saveImage(image: Bitmap): Uri? {
        val imagesFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(this, "com.example.art_run_android.fileProvider", file)
        } catch (e: IOException) {
            Log.d("image saving", "IOException while trying to write file for sharing: " + e.message)
        }
        return uri
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
}