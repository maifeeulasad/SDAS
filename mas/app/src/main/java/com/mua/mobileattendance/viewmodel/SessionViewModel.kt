package com.mua.mobileattendance.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mua.mobileattendance.retrofit.dto.*
import com.mua.mobileattendance.retrofit.service.impl.*

class SessionViewModel(application: Application) : BaseViewModel(application),
    RetrofitResponseListener {

    var attendanceRefreshing = MutableLiveData<Boolean>(false)

    var sessionDto = MutableLiveData<SessionDto>(SessionDto())
    var classroomDto = MutableLiveData<ClassroomDto>()
    var sessionEditing = MutableLiveData<Boolean>(false)
    var sessionAttend = MutableLiveData<Boolean>(false)
    var operationComplete = MutableLiveData<Boolean>(false)
    var role = MutableLiveData<RoleDto>(RoleDto.Student)
    var currentLocation = MutableLiveData<CoordinateDto>()
    var attendances: MutableLiveData<List<AttendanceDto>> = MutableLiveData(ArrayList())

    var isTeacher: LiveData<Boolean> = Transformations.map(role) {
        it == RoleDto.Teacher
    }


    private var createSessionService: SessionCreateServiceImpl =
        SessionCreateServiceImpl(application.baseContext, this)
    private var allAttendanceService: AttendanceAllServiceImpl =
        AttendanceAllServiceImpl(application.baseContext, this)
    private var attendAttendanceService: AttendanceAttendServiceImpl =
        AttendanceAttendServiceImpl(application.baseContext, this)


    fun createOrAttendSession(): View.OnClickListener {
        return View.OnClickListener {
            if (sessionEditing.value!!) {
                createSessionService.create(classroomDto.value!!.classroomId, sessionDto.value!!)
            } else {
                val attendance = AttendanceDto()
                attendance.coordinate = currentLocation.value
                attendance.code = sessionDto.value!!.code
                attendAttendanceService.attend(sessionDto.value!!.sessionId, attendance)
            }
        }
    }

    fun loadAttendances() {
        if (sessionDto.value!!.sessionId != null) {
            try {
                allAttendanceService.all(sessionDto.value!!.sessionId)
                attendanceRefreshing.postValue(true)
            } catch (e: Exception) { }
        }
    }


    fun mapBoundary(): ArrayList<CoordinateDto> {
        val res = ArrayList<CoordinateDto>()
        for (point in sessionDto.value!!.bounds) {
            res.add(CoordinateDto(point))
        }
        return res
    }


    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == SessionCreateServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is Long) {
                val sessionId = retrofitResponseObject.`object` as Long
                if (sessionId != -1L) {
                    addMessage("Session Creation Successful")
                    operationComplete.postValue(true)
                }
            }
        } else if (retrofitResponseObject.requestCode == AttendanceAllServiceImpl.TASK_ID) {
            attendanceRefreshing.postValue(false)
            attendances.postValue(retrofitResponseObject.`object` as ArrayList<AttendanceDto>)
        } else if (retrofitResponseObject.requestCode == AttendanceAttendServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` as Boolean) {
                addMessage("Successfully attended the session")
                operationComplete.postValue(true)
            }
        }
    }

}