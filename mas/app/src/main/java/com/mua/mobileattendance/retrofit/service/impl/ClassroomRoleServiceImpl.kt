package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.RoleDto
import com.mua.mobileattendance.retrofit.service.ClassroomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassroomRoleServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(ClassroomService::class.java)

    fun role(classroomId: Long) {
        val call = service.role(classroomId)

        call.enqueue(object : Callback<RoleDto> {
            override fun onResponse(call: Call<RoleDto>, response: Response<RoleDto>) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<RoleDto>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })

    }

    companion object {
        var TASK_ID = ClassroomRoleServiceImpl::class.java.name.hashCode()
    }
}