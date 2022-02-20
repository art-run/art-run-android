package com.example.art_run_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter_My(val profileList : ArrayList<SocialData>) : RecyclerView.Adapter<RecyclerAdapter_My.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter_My.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter_My.ViewHolder, position: Int) {
        holder.user_nickname.text= profileList.get(position).nickname
        holder.title.text= profileList.get(position).title
        holder.distance.text= profileList.get(position).distance
        holder.createdAt.text= profileList.get(position).createdAt


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
    }
}