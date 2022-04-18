package com.example.art_run_android.running

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.art_run_android.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class MapsFragment : Fragment() {

    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val REQUEST_PERMISSION_CODE = 1

    val DEFAULT_ZOOM_LEVEL = 16f

    private var defaultLocation = LatLng(37.5662952, 126.97794509999994)

    var currentLocation = defaultLocation

    private fun checkPermissions(): Boolean {

        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val undoPolylineList = mutableListOf<Polyline>()
    private val redoPolylineList = mutableListOf<MutableList<LatLng>>()
    private var lastLatLng: LatLng? = null

    lateinit var thisGoogleMap: GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.uiSettings.isMyLocationButtonEnabled = false

        when {
            checkPermissions() -> {
                googleMap.isMyLocationEnabled = true
                getMyLocation()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM_LEVEL))
            }
            else -> {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM_LEVEL))
            }
        }
        thisGoogleMap = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment_maps, container, false)
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location -> // GPS location can be null if GPS is switched off
                location?.let {
                    Log.d("위치",it.toString())
                    currentLocation = LatLng(it.latitude, it.longitude)
                    thisGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM_LEVEL))
                }
            }
            .addOnFailureListener { e ->
                Log.d("MapDemoActivity", "Error trying to get last GPS location")
                e.printStackTrace()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (!checkPermissions()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION_CODE)
        }
        mapFragment?.getMapAsync(callback)
    }

    fun getLatLngList(points: MutableList<Point>): MutableList<LatLng> {
        return mutableListOf<LatLng>().apply {
            if (lastLatLng != null && undoPolylineList.isNotEmpty()) {
                this.add(lastLatLng!!)
            }
            points.forEach {
                this.add(thisGoogleMap.projection.fromScreenLocation(it))
            }
        }
    }

    fun drawPolyline(polyline: MutableList<LatLng>, isRedo: Boolean) {
        thisGoogleMap.addPolyline(PolylineOptions().clickable(true).addAll(polyline)).apply {
            undoPolylineList.add(this)
        }
        lastLatLng = polyline.last()
        if (!isRedo) {
            redoPolylineList.clear()
        }
    }

    fun undoPolyline() {
        if (undoPolylineList.isNotEmpty()) {
            val polyline = undoPolylineList.last()
            undoPolylineList.remove(polyline)
            redoPolylineList.add(polyline.points)
            polyline.remove()
            if (undoPolylineList.isNotEmpty()) {
                lastLatLng = undoPolylineList.last().points.last()
            }
        }
    }

    fun redoPolyline() {
        if (redoPolylineList.isNotEmpty()) {
            val polyline = redoPolylineList.last()
            redoPolylineList.remove(polyline)
            drawPolyline(polyline, true)
        }
    }

}