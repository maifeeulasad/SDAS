package com.mua.mobileattendance.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mua.mobileattendance.R
import com.mua.mobileattendance.listener.AttendanceItemClickListener
import com.mua.mobileattendance.retrofit.dto.AttendanceDto
import com.mua.mobileattendance.retrofit.dto.StateDto
import java.util.*

class AttendanceRecyclerViewAdapter(
    private val context: Activity,
    private val attendanceDtos: ArrayList<AttendanceDto>,
    private val itemClickListener: AttendanceItemClickListener,
    private val editable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    constructor(
        context: Activity,
        attendanceDtos: ArrayList<AttendanceDto>,
        itemClickListener: AttendanceItemClickListener
    ) : this(context, attendanceDtos, itemClickListener, false)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val attendanceDto = attendanceDtos[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.tv_userid.text = attendanceDto.userId.toString()
        viewHolder.tv_username.text = attendanceDto.username.toString()
        if (editable) {
            viewHolder.tv_state.visibility = View.GONE
            viewHolder.sp_state.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    val state = StateDto.fromValue(viewHolder.sp_state.selectedItem.toString())
                    if (state != StateDto.Pending) {
                        itemClickListener.onStateChange(attendanceDto, state)
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            })
        } else {
            viewHolder.sp_state.visibility = View.GONE
            viewHolder.tv_state.text = attendanceDto.state.toString()
        }
        viewHolder.tv_coordinate.text = attendanceDto.coordinate.toString()
        viewHolder.tv_time.text = attendanceDto.attendanceTime.toGMTString()
    }

    override fun getItemCount(): Int {
        return attendanceDtos.size
    }


    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_userid: TextView
        var tv_username: TextView
        var tv_coordinate: TextView
        var tv_state: TextView
        var sp_state: Spinner
        var tv_time: TextView

        init {
            tv_userid = itemView.findViewById(R.id.tv_item_attendance_userid)
            tv_username = itemView.findViewById(R.id.tv_item_attendance_username)
            tv_coordinate = itemView.findViewById(R.id.tv_item_attendance_coordinate)
            tv_state = itemView.findViewById(R.id.tv_item_attendance_state)
            sp_state = itemView.findViewById(R.id.sp_item_attendance_state_edit)
            tv_time = itemView.findViewById(R.id.tv_item_attendance_time)
            itemView.setOnClickListener { view: View? -> itemClickListener.onClick(attendanceDtos[adapterPosition]) }
        }
    }
}