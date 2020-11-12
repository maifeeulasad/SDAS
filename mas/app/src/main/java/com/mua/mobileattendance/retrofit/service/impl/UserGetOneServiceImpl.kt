package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.UserDto
import com.mua.mobileattendance.retrofit.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserGetOneServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(UserService::class.java)

    fun getOne(userId: Long) {
        val call = service.getOne(userId)

        call.enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }
        })
    }

    companion object {
        var TASK_ID = UserGetOneServiceImpl::class.java.name.hashCode()
    }
}