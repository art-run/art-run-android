package com.example.art_run_android

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.art_run_android.databinding.ItemSocialBinding
import com.example.art_run_android.running.DrawRouteView
import com.example.art_run_android.running.MapsFragment
import com.example.art_run_android.running.OsrmClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RecyclerAdapter_Social(val profileList : MutableList<SocialData>,val context: Context) : RecyclerView.Adapter<RecyclerAdapter_Social.ViewHolder>(),
    OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var lat: Double? = null
    var lon: Double? = null
    //private var mFragmentManager : FragmentManager = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter_Social.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_social,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nickname.text= profileList.get(position).nickname
        holder.title.text= profileList.get(position).title
        holder.distance.text= profileList.get(position).distance
        holder.createdAt.text= profileList.get(position).createdAt
        var url=profileList.get(position).profileImg
        Glide.with(context).load(url).into(holder.userImage)
        //val polyline = holder.mMap.addPolyline(profileList.get(position).polylineOptions)
        //holder.map.addPolyline(profileList.get(position).polylineOptions)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    //layout의 view와 매칭해놓은 틀, 받아온 데이터를 계속 적용시킬 뷰를 어디가지 못하게 꽉 잡고있음.
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nickname = itemView.findViewById<TextView>(R.id.user_nickname)
        val title = itemView.findViewById<TextView>(R.id.item_title)
        val distance = itemView.findViewById<TextView>(R.id.item_detail)
        val createdAt = itemView.findViewById<TextView>(R.id.item_date)
        val userImage= itemView.findViewById<ImageView>(R.id.user_image)
        var map = itemView.findViewById<MapView>(R.id.mapView5)
        //val wktRunRoute = = itemView.findViewById<TextView>(R.id.item_image)
        //val drawRouteLayout = itemView.findViewById<FrameLayout>(R.id.drawRouteLayout)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //val polyline = mMap.addPolyline(profileList.get(position).polylineOptions)
        val marker = LatLng(lat!!, lon!!)
        mMap.addMarker(MarkerOptions().position(marker).title("마커 제목"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }
}
