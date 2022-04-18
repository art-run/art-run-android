package com.example.art_run_android.running

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.art_run_android.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

class CourseAdapter(private val context: Context) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    var datas = mutableListOf<RecommendedRoute>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.running_item_courserun,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

        private val txtName: TextView = itemView.findViewById(R.id.courseName)
        private val txtDistance: TextView = itemView.findViewById(R.id.courseDist)
        private val mapView: MapView = itemView.findViewById(R.id.courseMap)
        private lateinit var map: GoogleMap
        private var latLng = LatLng(37.5662952, 126.97794509999994)

        init {
            with(mapView) {
                onCreate(null)
                getMapAsync(this@ViewHolder)
            }
        }

        private fun setMapLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                mapType = GoogleMap.MAP_TYPE_NORMAL
                setOnMapClickListener {
                }
            }
        }

        fun bind(item: RecommendedRoute) {
            txtName.text = item.title
            txtDistance.text = item.distance.toString()
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(context)
            // If map is not initialised properly
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            setMapLocation()
        }
    }
}