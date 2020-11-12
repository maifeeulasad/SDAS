package com.mua.mobileattendance.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.adapter.AttendanceRecyclerViewAdapter
import com.mua.mobileattendance.databinding.ActivitySessionBinding
import com.mua.mobileattendance.listener.AttendanceItemClickListener
import com.mua.mobileattendance.manager.ActivityManager
import com.mua.mobileattendance.retrofit.dto.*
import com.mua.mobileattendance.viewmodel.SessionViewModel
import java.util.*

class SessionActivity : BaseActivity(), AttendanceItemClickListener {

    private lateinit var mBinding: ActivitySessionBinding
    private lateinit var viewModel: SessionViewModel

    private lateinit var mAdapter: AttendanceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        //todo : search

        setMinimizable(false)
        setSecureScreen()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        initEditAttendance()

        viewModel.isTeacher.observe(this, Observer {
            if (it) {
                try {
                    ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
                } catch (e: Exception) {
                }
            } else {
                try {
                    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
                } catch (e: Exception) {
                }
            }
        })

        mBinding.srlSessionParent.setOnRefreshListener {
            viewModel.loadAttendances()
        }

        viewModel.attendanceRefreshing.observe(this, Observer {
            mBinding.srlSessionParent.isRefreshing = it
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAttendances()
    }

    fun initEditAttendance() {
        mBinding.btnAttendanceEdit.setOnClickListener {
            if (viewModel.isTeacher.value == true) {
                val intent = Intent(this, ManageAttendanceActivity::class.java)

                val bundle = Bundle()
                //bundle.putLong("classroomid", viewModel.classroomDto.value!!.classroomId)
                bundle.putLong("sessionid", viewModel.sessionDto.value!!.sessionId)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setMinimizable(true)
        try {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityManager.MAP_ON_SUCCESS_REQUEST_CODE) {
            if (resultCode == ActivityManager.MAP_ON_SUCCESS_RESULT_CODE) {
                val points = data!!.extras!!.getParcelableArrayList<CoordinateDto>("locations")
                val point = data.extras!!.getParcelable<CoordinateDto>("location")
                viewModel.sessionDto.value!!.bounds = points
                viewModel.currentLocation.postValue(point!!)
            }
        }
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_session)
        viewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        mBinding.session = viewModel
        mBinding.lifecycleOwner = this


        initFromChild(viewModel)


        try {
            viewModel.classroomDto.postValue(intent.extras!!.getSerializable("classroom") as ClassroomDto)
        } catch (e: Exception) {
        }
        try {
            viewModel.sessionDto.postValue(intent.extras!!.getSerializable("session") as SessionDto)
        } catch (e: Exception) {
        }
        try {
            viewModel.sessionEditing.postValue(intent.extras!!.getBoolean("editable"))
        } catch (e: Exception) {
            viewModel.sessionEditing.postValue(false)
        }
        try {
            val role = RoleDto.fromValue(intent.extras!!.getString("role"))
            viewModel.sessionAttend
                .postValue(
                    role == RoleDto.Student
                            || role == RoleDto.CR
                            || role == RoleDto.Teacher
                )
            viewModel.role.postValue(role)
        } catch (e: Exception) {
        }

        initOnComplete()


        mBinding.btnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelableArrayList(
                "locations",
                viewModel.mapBoundary()
            )
            bundle.putBoolean("editable", viewModel.sessionEditing.value!!)
            intent.putExtras(bundle)

            startActivityForResult(intent, ActivityManager.MAP_ON_SUCCESS_REQUEST_CODE)
        }

        initTitle()
        initLoadAttendances()
        initAttendanceList()
    }


    fun initAttendanceList() {
        mAdapter = AttendanceRecyclerViewAdapter(this, ArrayList(), this)
        mBinding.rvAttendances.adapter = mAdapter
        mBinding.rvAttendances.setLayoutManager(LinearLayoutManager(this))


        viewModel.attendances.observe(this, Observer {
            mAdapter = AttendanceRecyclerViewAdapter(this, it as ArrayList<AttendanceDto>, this)
            mBinding.rvAttendances.adapter = mAdapter
        })
    }

    fun initLoadAttendances() {
        viewModel.sessionDto.observe(this, Observer {
            viewModel.loadAttendances()
        })
    }

    fun initTitle() {
        viewModel.sessionEditing.observe(this, Observer {
            if (it) {
                setTitle("Create session")
            } else {
                setTitle("Attend")
            }
        })
    }

    fun initOnComplete() {
        viewModel.operationComplete.observe(this, Observer {
            if (it) {
                finish()
            }
        })
    }

    override fun onClick(attendanceDto: AttendanceDto?) {
        if (viewModel.role.value == RoleDto.Teacher) {

        }
    }

    override fun onStateChange(attendanceDto: AttendanceDto?, stateDto: StateDto?) {
        TODO("Not yet implemented")
    }

}