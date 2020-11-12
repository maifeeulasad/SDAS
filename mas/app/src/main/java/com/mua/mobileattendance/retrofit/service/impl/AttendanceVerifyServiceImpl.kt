package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.StateDto
import com.mua.mobileattendance.retrofit.service.AttendanceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceVerifyServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(AttendanceService::class.java)

    fun verify(sessionId: Long, attendanceId: Long, state: StateDto) {
        val call = service.verify(sessionId, attendanceId, state)

        call.enqueue(object : Callback<Boolean> {

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })

    }

    companion object {
        var TASK_ID = AttendanceVerifyServiceImpl::class.java.name.hashCode()
    }
}