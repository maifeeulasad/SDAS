package com.mua.mobileattendance.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.AttendanceRecyclerViewAdapter
import com.mua.mobileattendance.listener.AttendanceItemClickListener
import com.mua.mobileattendance.retrofit.dto.AttendanceDto
import com.mua.mobileattendance.retrofit.dto.StateDto
import com.mua.mobileattendance.retrofit.service.impl.AttendanceAllServiceImpl
import com.mua.mobileattendance.retrofit.service.impl.AttendanceVerifyServiceImpl
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseListener
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseObject
import kotlin.properties.Delegates

class ManageAttendanceActivity : BaseActivity(), RetrofitResponseListener,
    AttendanceItemClickListener {

    lateinit var context: Context
    var classroomId by Delegates.notNull<Long>()
    var sessionId by Delegates.notNull<Long>()
    lateinit var allAttendanceService: AttendanceAllServiceImpl
    lateinit var verifyAttendanceService: AttendanceVerifyServiceImpl
    var attendances = ArrayList<AttendanceDto>()
    lateinit var mUserClassroomRoleAdapter: AttendanceRecyclerViewAdapter
    lateinit var rvAttendanceEdit: RecyclerView
    lateinit var ibDone: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_attendance)

        init()
    }

    fun init() {
        context = this

        rvAttendanceEdit = findViewById(R.id.rv_attendances_edit)
        ibDone = findViewById(R.id.ib_done_edit_attendance)

        try {
            classroomId = intent.extras!!.getLong("classroomid")
        } catch (e: Exception) {
        }
        try {
            sessionId = intent.extras!!.getLong("sessionid")
        } catch (e: Exception) {
        }
        allAttendanceService = AttendanceAllServiceImpl(this, this)
        verifyAttendanceService = AttendanceVerifyServiceImpl(this, this)
        rvAttendanceEdit.setLayoutManager(LinearLayoutManager(this))
        initLoadAttendances()

        allAttendanceService.all(sessionId)

        initDone()

    }

    fun initDone() {
        ibDone.setOnClickListener {
            finish()
        }
    }

    fun initLoadAttendances() {
        mUserClassroomRoleAdapter = AttendanceRecyclerViewAdapter(this, attendances, this, true)
        rvAttendanceEdit.adapter = mUserClassroomRoleAdapter
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == AttendanceAllServiceImpl.TASK_ID) {
            attendances = retrofitResponseObject.`object` as ArrayList<AttendanceDto>
            initLoadAttendances()
        } else if (retrofitResponseObject.requestCode == AttendanceVerifyServiceImpl.TASK_ID) {
            var res = if (retrofitResponseObject.`object` as Boolean)
                "Attendance has been edited successfully"
            else
                "Attendance update unsuccessful"

            Toast.makeText(this, "Role has been changed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClick(attendanceDto: AttendanceDto?) {
        Log.d("d--mua", attendanceDto.toString())
    }

    override fun onStateChange(attendanceDto: AttendanceDto?, stateDto: StateDto?) {
        verifyAttendanceService.verify(sessionId, attendanceDto!!.attendanceId, stateDto!!)
    }

}