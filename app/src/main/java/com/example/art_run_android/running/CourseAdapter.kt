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
import com.google.android.gms.maps.model.PolylineOptions

class CourseAdapter(private val context: Context) :
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    var recommendedRoutes = mutableListOf<RecommendedRoute>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.running_item_courserun, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recommendedRoutes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recommendedRoutes[position])
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: RecommendedRoute)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

        private val txtName: TextView = itemView.findViewById(R.id.courseName)
        private val txtDistance: TextView = itemView.findViewById(R.id.courseDist)
        private val mapView: MapView = itemView.findViewById<MapView?>(R.id.courseMap).apply {
            this.isClickable = false
        }
        private lateinit var map: GoogleMap
        private var centerLatLng = LatLng(37.5662952, 126.97794509999994)
        private var polyline = mutableListOf<LatLng>()
        private var recommendationId = -1

        init {
            with(mapView) {
                onCreate(null)
                getMapAsync(this@ViewHolder)
            }
        }

        private fun setMapLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 15f))
                mapType = GoogleMap.MAP_TYPE_NORMAL
                addPolyline(PolylineOptions().addAll(polyline))
            }
        }

        fun bind(item: RecommendedRoute) {
            txtName.text = item.title
            txtDistance.text = item.distance.toString()
            polyline = wktToPolyline(item.wktRoute)
            centerLatLng = calculateCenterPosition(polyline)
            recommendationId = item.id
            setMapLocation()

            itemView.setOnClickListener {
                listener?.onItemClick(itemView,item)
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(context)
            // If map is not initialised properly
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            setMapLocation()
        }

        private fun wktToPolyline(wktRoute: String): MutableList<LatLng> {
            val prim1 = wktRoute.substring(wktRoute.indexOf('(') + 1, wktRoute.indexOf(')'))
            val prim2 = prim1.split(", ")
            val polyline = mutableListOf<LatLng>()
            prim2.forEach {
                val prim3 = it.split(" ")
                if (prim3.size >= 2) {
                    val latLng = LatLng(prim3[1].toDouble(), prim3[0].toDouble())
                    polyline.add(latLng)
                }
            }

            return polyline
        }

        private fun calculateCenterPosition(polyline: MutableList<LatLng>): LatLng {
            val maxLat = polyline.maxOf { it.latitude }
            val minLat = polyline.minOf { it.latitude }
            val maxLng = polyline.maxOf { it.longitude }
            val minLng = polyline.minOf { it.longitude }

            return LatLng((maxLat + minLat) / 2, (maxLng + minLng) / 2)
        }
    }
}