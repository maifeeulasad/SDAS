package com.mua.mobileattendance.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mua.mobileattendance.R
import com.mua.mobileattendance.listener.ClassroomItemClickListener
import com.mua.mobileattendance.retrofit.dto.ClassroomDto
import java.util.*

class ClassroomRecyclerViewAdapter(
    private val context: Activity,
    private val classroomDtos: ArrayList<ClassroomDto>,
    private var itemClickListener: ClassroomItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_classroom, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val classroomDto = classroomDtos[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.tv_title.text = classroomDto.name
        viewHolder.tv_details.text = classroomDto.details
        val date = classroomDto.nextSessionTime
        if (date != null) {
            viewHolder.tv_next_date.text = date.toGMTString()
        }
    }

    override fun getItemCount(): Int {
        return classroomDtos.size
    }


    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_title: TextView
        var tv_details: TextView
        var tv_next_date: TextView

        init {
            tv_title = itemView.findViewById(R.id.tv_item_classroom_title)
            tv_details = itemView.findViewById(R.id.tv_item_classroom_details)
            tv_next_date = itemView.findViewById(R.id.tv_item_classroom_next_date)
            itemView.setOnClickListener { view: View? -> itemClickListener.onClick(classroomDtos[adapterPosition]) }
        }
    }
}