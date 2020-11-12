package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.SessionDto
import com.mua.mobileattendance.retrofit.service.ClassroomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassroomSessionsServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(ClassroomService::class.java)

    fun sessions(classroomId: Long) {
        val call = service.sessions(classroomId)

        call.enqueue(object : Callback<List<SessionDto>> {
            override fun onResponse(
                call: Call<List<SessionDto>>,
                response: Response<List<SessionDto>>
            ) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<List<SessionDto>>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })

    }

    companion object {
        var TASK_ID = ClassroomSessionsServiceImpl::class.java.name.hashCode()
    }
}