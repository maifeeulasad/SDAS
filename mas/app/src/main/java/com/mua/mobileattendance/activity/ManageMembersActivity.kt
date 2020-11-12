package com.mua.mobileattendance.activity

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.UserClassroomRoleRecyclerViewAdapter
import com.mua.mobileattendance.listener.UserClassroomRoleItemClickListener
import com.mua.mobileattendance.retrofit.dto.RoleDto
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto
import com.mua.mobileattendance.retrofit.service.impl.*
import kotlin.properties.Delegates

class ManageMembersActivity : BaseActivity(), RetrofitResponseListener,
    UserClassroomRoleItemClickListener {

    var classroomId by Delegates.notNull<Long>()
    var roles = ArrayList<UserClassroomRoleDto>()
    lateinit var rvMembersEdit: RecyclerView
    lateinit var ibDone: ImageButton
    lateinit var mUserClassroomRoleAdapter: UserClassroomRoleRecyclerViewAdapter

    lateinit var rolesClassroomServiceImpl: ClassroomRolesServiceImpl
    lateinit var assignClassroomServiceImpl: ClassroomAssignServiceImpl
    lateinit var removeClassroomServiceImpl: ClassroomRemoveServiceImpl
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_members)

        init()
    }

    fun init() {
        Toast.makeText(this,"Left swipe someone to remove",Toast.LENGTH_SHORT).show()

        context = this
        classroomId = intent.extras!!.getLong("classroomid")
        rvMembersEdit = findViewById(R.id.rv_members_edit)
        ibDone = findViewById(R.id.ib_done_edit)

        rolesClassroomServiceImpl = ClassroomRolesServiceImpl(this, this)
        assignClassroomServiceImpl = ClassroomAssignServiceImpl(this, this)
        removeClassroomServiceImpl = ClassroomRemoveServiceImpl(this, this)
        rolesClassroomServiceImpl.roles(classroomId)

        rvMembersEdit.setLayoutManager(LinearLayoutManager(this))
        initLoadRoles()

        ibDone.setOnClickListener {
            finish()
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                v: RecyclerView,
                h: RecyclerView.ViewHolder,
                t: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                if (roles.size == 1) {
                    Toast.makeText(context, "At least one member has to remain", Toast.LENGTH_LONG)
                        .show()
                } else {
                    remove(h.adapterPosition)
                }
            }
        }).attachToRecyclerView(rvMembersEdit)

    }

    fun remove(index: Int) {
        val role = roles.get(index)
        roles.removeAt(index)
        //mUserClassroomRoleAdapter.removeAt(index)
        removeClassroomServiceImpl.removeUsername(classroomId, role.username)
    }


    fun initLoadRoles() {
        mUserClassroomRoleAdapter = UserClassroomRoleRecyclerViewAdapter(this, roles, this, true)
        rvMembersEdit.adapter = mUserClassroomRoleAdapter
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == ClassroomRolesServiceImpl.TASK_ID) {
            roles = retrofitResponseObject.`object` as ArrayList<UserClassroomRoleDto>
            initLoadRoles()
        }
    }

    override fun onClick(userClassroomRoleDto: UserClassroomRoleDto?) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(userClassroomRoleDto: UserClassroomRoleDto?) {
        TODO("Not yet implemented")
    }

    override fun onRoleChange(userClassroomRoleDto: UserClassroomRoleDto?, role: RoleDto?) {
        assignClassroomServiceImpl.assignUserId(classroomId, userClassroomRoleDto!!.userId, role!!)
    }

}