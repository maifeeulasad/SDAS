package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto
import com.mua.mobileattendance.retrofit.service.ClassroomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassroomRolesServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(ClassroomService::class.java)

    fun roles(classroomId: Long) {
        val call = service.roles(classroomId)

        call.enqueue(object : Callback<List<UserClassroomRoleDto>> {
            override fun onResponse(
                call: Call<List<UserClassroomRoleDto>>,
                response: Response<List<UserClassroomRoleDto>>
            ) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<List<UserClassroomRoleDto>>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })

    }

    companion object {
        var TASK_ID = ClassroomRolesServiceImpl::class.java.name.hashCode()
    }
}