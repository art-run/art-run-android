package com.example.art_run_android

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.art_run_android.running.DrawRouteView
import com.example.art_run_android.running.MapsFragment
import com.example.art_run_android.running.OsrmClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RecyclerAdapter_Social(val profileList : ArrayList<SocialData>
                             , val context: Context?, fragmentManager : FragmentManager
) : RecyclerView.Adapter<RecyclerAdapter_Social.ViewHolder>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var lat: Double? = null
    var lon: Double? = null
    private var mFragmentManager : FragmentManager = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter_Social.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_social,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter_Social.ViewHolder, position: Int) {
        holder.user_nickname.text= profileList.get(position).nickname
        holder.title.text= profileList.get(position).title
        holder.distance.text= profileList.get(position).distance
        holder.createdAt.text= profileList.get(position).createdAt

        val transaction = mFragmentManager.beginTransaction()
        val mapsFragment = MapsFragment()
        transaction.add(R.id.mapView2, mapsFragment)
        transaction.commit()

        val drawRouteView = DrawRouteView(context)
        holder.drawRouteLayout.addView(drawRouteView)

        drawRouteView.SetStrokeListener(object : DrawRouteView.StrokeListener {
            override fun onStrokeEvent(pointList: MutableList<Point>) {
                val list = mapsFragment.getLatLngList(pointList)
                val string = StringBuilder()
//                list.forEach {
//                    string.append(it.longitude)
//                    string.append(",")
//                    string.append(it.latitude)
//                    if (list.indexOf(it) != list.size - 1) {
//                        string.append(";")
//                    }
//                }
                list.forEach {
                    string.append(35.8554266)
                    string.append(",")
                    string.append(128.7521366)
                    if (list.indexOf(it) != list.size - 1) {
                        string.append(";")
                    }
                }

                val callGetMatch = OsrmClient.matchApiService.getMatch(string.toString())

//                callGetMatch.enqueue(object : Callback<RouteDataClass> {
//                    override fun onResponse(
//                        call: Call<RouteDataClass>,
//                        response: Response<RouteDataClass>
//                    ) {
//                        if (response.isSuccessful) { // <--> response.code == 200
//                            val match = response.body() as RouteDataClass
//                            match.routes.forEach {
//                                val polyline = PolyUtil.decode(it.geometry)
//                                mapsFragment.drawPolyline(polyline, false, false)
//                            }
//
//                        } else { // code == 400
//                            // 실패 처리
//                        }
//                    }
//
//                    override fun onFailure(call: Call<RouteDataClass>, t: Throwable) {
//                        TODO("Not yet implemented")
//                    }
//                })


            }
        })
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //        val img = itemView.findViewById<ImageView>(R.id.user_image)
        val user_nickname = itemView.findViewById<TextView>(R.id.user_nickname)
        val title = itemView.findViewById<TextView>(R.id.item_title)
        val distance = itemView.findViewById<TextView>(R.id.item_detail)
        val createdAt = itemView.findViewById<TextView>(R.id.item_date)
        // val wktRunRoute = = itemView.findViewById<TextView>(R.id.item_image)
        val drawRouteLayout = itemView.findViewById<FrameLayout>(R.id.drawRouteLayout)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker = LatLng(lat!!, lon!!)
        mMap.addMarker(MarkerOptions().position(marker).title("마커 제목"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }
}