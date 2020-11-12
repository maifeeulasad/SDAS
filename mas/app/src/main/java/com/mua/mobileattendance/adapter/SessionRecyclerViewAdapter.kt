package com.mua.mobileattendance.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mua.mobileattendance.R
import com.mua.mobileattendance.listener.SessionItemClickListener
import com.mua.mobileattendance.retrofit.dto.SessionDto
import java.util.*

class SessionRecyclerViewAdapter(
    private val context: Activity,
    private val sessionDtos: ArrayList<SessionDto>,
    private var itemClickListener: SessionItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sessionDto = sessionDtos[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.tv_weight.text = sessionDto.weight.toString()
        viewHolder.tv_minutes.text = sessionDto.minutes.toString()
        val date = sessionDto.creationTime
        if (date != null) {
            viewHolder.tv_date.text = date.toGMTString()
            viewHolder.tv_remaining.text = remainingTime(date, sessionDto.minutes)
        }
    }

    override fun getItemCount(): Int {
        return sessionDtos.size
    }

    fun addMinutesToDate(date: Date?, minutes: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.MINUTE, minutes.toInt())
        return calendar.time
    }

    fun remainingTime(date: Date, minutes: Long): String {
        val targetDate = addMinutesToDate(date, minutes)
        val currentDate = Calendar.getInstance().time
        val diff = (targetDate.time - currentDate.time) / 1000
        if (diff <= 0) {
            return "No remaining time"
        }
        return "$diff seconds remaining"
    }


    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_weight: TextView
        var tv_minutes: TextView
        var tv_date: TextView
        var tv_remaining: TextView

        init {
            tv_weight = itemView.findViewById(R.id.tv_item_session_weight)
            tv_minutes = itemView.findViewById(R.id.tv_item_session_minute)
            tv_date = itemView.findViewById(R.id.tv_item_session_date)
            tv_remaining = itemView.findViewById(R.id.tv_item_session_time_remaining)
            itemView.setOnClickListener { view: View? -> itemClickListener.onClick(sessionDtos[adapterPosition]) }
        }
    }
}