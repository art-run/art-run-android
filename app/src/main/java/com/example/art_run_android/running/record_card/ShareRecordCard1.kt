package com.example.art_run_android.running.record_card

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.art_run_android.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.running_activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ShareRecordCard1 : AppCompatActivity() {
    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_record_card1)
        val showmap: Switch = findViewById(R.id.share_show_map)
        val showstats : Switch = findViewById(R.id.share_show_statistic)
        val back : ImageButton = findViewById(R.id.share_back)
        val goto_share: ImageButton = findViewById(R.id.share_select_SNS)
        val frameLayout : FrameLayout = findViewById(R.id.ShareView)
        lateinit var bm:Bitmap

        val callback = OnMapReadyCallback { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5662952, 126.97794509999994), 16f))
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
        }

        val mapView: MapView = findViewById<MapView?>(R.id.ShareMap).apply {
            this.isClickable = false
            this.onCreate(null)
            this.getMapAsync(callback)
        }
/*
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

 */
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
        showmap.setChecked(true)
        showmap.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"지도 표시",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"지도 표시 해제",Toast.LENGTH_SHORT).show()
            }
        }

        // 통계 표시 스위치 , 기본으로 표시 해놓는다.
        showstats.setOnCheckedChangeListener { CompoundButton, onSwitch ->
            if(onSwitch){
                Toast.makeText(applicationContext,"통계 표시",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"통계 표시 해제",Toast.LENGTH_SHORT).show()
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
        //TODO - Should be processed in another thread
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
}