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
import com.mua.mobileattendance.listener.UserClassroomRoleItemClickListener
import com.mua.mobileattendance.retrofit.dto.RoleDto
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto

class UserClassroomRoleRecyclerViewAdapter(
    private val context: Activity,
    private val userClassroomRoleDtos: ArrayList<UserClassroomRoleDto>,
    private var itemClickListener: UserClassroomRoleItemClickListener,
    private val editable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.item_user_classroom_role, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    constructor(
        context: Activity,
        userClassroomRoleDtos: ArrayList<UserClassroomRoleDto>,
        itemClickListener: UserClassroomRoleItemClickListener
    )
            : this(context, userClassroomRoleDtos, itemClickListener, false)

    fun removeAt(index: Int) {
        userClassroomRoleDtos.removeAt(index)
        notifyItemRemoved(index)
        notifyItemChanged(index)
        notifyItemRangeChanged(index, userClassroomRoleDtos.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userClassroomRoleDto = userClassroomRoleDtos[position]
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.tv_user_id.text = userClassroomRoleDto.userId.toString()
        viewHolder.tv_user_name.text = userClassroomRoleDto.username
        if (editable) {
            viewHolder.tv_role.visibility = View.GONE
            viewHolder.sp_role.visibility = View.VISIBLE
            val role = userClassroomRoleDto.role.role
            val index = Math
                .max(
                    0,
                    context.resources.getStringArray(R.array.array_role_edit).indexOf(role)
                )


            viewHolder.sp_role.setSelection(index)


            viewHolder.sp_role.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    val role = RoleDto.fromValue(viewHolder.sp_role.selectedItem.toString())
                    if (role != RoleDto.Pending) {
                        itemClickListener.onRoleChange(userClassroomRoleDto, role)
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            })


        } else {
            viewHolder.tv_role.visibility = View.VISIBLE
            viewHolder.sp_role.visibility = View.GONE
            viewHolder.tv_role.text = userClassroomRoleDto.role.role
        }
    }

    override fun getItemCount(): Int {
        return userClassroomRoleDtos.size
    }


    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_user_id: TextView
        var tv_user_name: TextView
        var tv_role: TextView
        var sp_role: Spinner

        init {
            tv_user_id = itemView.findViewById(R.id.tv_item_ucr_user_id)
            tv_user_name = itemView.findViewById(R.id.tv_item_ucr_user_name)
            tv_role = itemView.findViewById(R.id.tv_item_ucr_role)
            sp_role = itemView.findViewById(R.id.sp_item_ucr_role)
            itemView.setOnClickListener {
                itemClickListener.onClick(userClassroomRoleDtos[adapterPosition])
            }
            itemView.setOnLongClickListener {
                itemClickListener.onLongClick(userClassroomRoleDtos[adapterPosition])
                true
            }


        }
    }
}