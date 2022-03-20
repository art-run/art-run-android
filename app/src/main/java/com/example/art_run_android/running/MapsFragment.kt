package com.example.art_run_android.running

import android.Manifest
import android.graphics.Point
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.art_run_android.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    private val undoPolylineList = mutableListOf<Polyline>()
    private val redoPolylineList = mutableListOf<MutableList<LatLng>>()
    private var lastLatLng: LatLng? = null

    lateinit var thisGoogleMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Current Loacation"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM_LEVEL))
        thisGoogleMap = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
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