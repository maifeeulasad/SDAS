package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEditServiceImpl(context: Context?, retrofitResponseListener: RetrofitResponseListener?) :
    BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(UserService::class.java)

    fun edit(name: String, email: String) {
        val call = service.edit(name, email)

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
        var TASK_ID = UserEditServiceImpl::class.java.name.hashCode()
    }
}