package com.example.art_run_android.running

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.art_run_android.R

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtName: TextView = itemView.findViewById(R.id.courseName)
        private val txtDistance: TextView = itemView.findViewById(R.id.courseDist)

        fun bind(item: RecommendedRoute) {
            txtName.text = item.title
            txtDistance.text = item.distance.toString()
        }
    }
}