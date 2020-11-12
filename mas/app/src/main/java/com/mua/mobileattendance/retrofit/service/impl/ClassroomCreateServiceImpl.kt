package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.service.ClassroomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassroomCreateServiceImpl(
    context: Context?,
    retrofitResponseListener: RetrofitResponseListener?
) : BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(ClassroomService::class.java)

    fun create(name: String, details: String, role: String) {
        val call = service.create(name, details, role)

        call.enqueue(object : Callback<Long> {

            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }


            override fun onFailure(call: Call<Long>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }

        })
    }

    companion object {
        var TASK_ID = ClassroomCreateServiceImpl::class.java.name.hashCode()
    }
}