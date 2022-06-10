package com.example.art_run_android

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolylineOptions

class SocialAdapter(private val context: Context) :
    RecyclerView.Adapter<SocialAdapter.ViewHolder>() {

    var socialRoutes = mutableListOf<SocialDClass>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.social_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = socialRoutes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(socialRoutes[position])
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: SocialDClass)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

        val nickname: TextView = itemView.findViewById(R.id.user_nickname)
        val title: TextView = itemView.findViewById(R.id.item_title)
        val distance: TextView = itemView.findViewById(R.id.item_detail)
        private val createdAt: TextView = itemView.findViewById(R.id.item_date)
        private val userImage: ImageView = itemView.findViewById(R.id.user_image)
        private val mapView: MapView = itemView.findViewById<MapView?>(R.id.rvSocialMV).apply {
            this.isClickable = false
        }
        private lateinit var map: GoogleMap
        private var centerLatLng = LatLng(37.5662952, 126.97794509999994)
        private var polyline = mutableListOf<LatLng>()
        private var routeId = -1

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
                map.clear()
                addPolyline(PolylineOptions().addAll(polyline))
            }
        }

        fun bind(item: SocialDClass) {
            nickname.text = item.nickname
            title.text = item.title
            distance.text = item.distance.toString() + "m"
            createdAt.text = item.createdAt
            Glide.with(context).load(item.profileImg).into(userImage)
            polyline = wktToPolyline(item.wktRunRoute)
            centerLatLng = calculateCenterPosition(polyline)
            routeId = item.routeId
            setMapLocation()

            itemView.setOnClickListener {
                listener?.onItemClick(itemView,item)
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            MapsInitializer.initialize(context)
            // If map is not initialised properly
            try {
                val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style1))

                if (!success) {
                    Log.e("loading Style", "Style parsing failed.");
                }
            } catch (e : Resources.NotFoundException) {
                Log.e("loading Style", "Can't find style. Error: ", e);
            }
            map = googleMap
            map.uiSettings.isMapToolbarEnabled = false
            setMapLocation()
        }

        private fun calculateCenterPosition(polyline: MutableList<LatLng>): LatLng {
            val maxLat = polyline.maxOf { it.latitude }
            val minLat = polyline.minOf { it.latitude }
            val maxLng = polyline.maxOf { it.longitude }
            val minLng = polyline.minOf { it.longitude }

            return LatLng((maxLat + minLat) / 2, (maxLng + minLng) / 2)
        }
    }

    fun wktToPolyline(wktRoute: String): MutableList<LatLng> {
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