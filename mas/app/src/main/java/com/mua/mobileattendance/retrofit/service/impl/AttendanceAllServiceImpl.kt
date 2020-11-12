package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.AttendanceDto
import com.mua.mobileattendance.retrofit.service.AttendanceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceAllServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(AttendanceService::class.java)

    fun all(sessionId: Long) {
        val call = service.all(sessionId)

        call.enqueue(object : Callback<List<AttendanceDto>> {

            override fun onResponse(
                call: Call<List<AttendanceDto>>,
                response: Response<List<AttendanceDto>>
            ) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<List<AttendanceDto>>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })

    }

    companion object {
        var TASK_ID = AttendanceAllServiceImpl::class.java.name.hashCode()
    }
}