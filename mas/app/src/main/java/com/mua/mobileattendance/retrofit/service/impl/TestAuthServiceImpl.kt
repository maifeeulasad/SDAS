package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.service.TestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestAuthServiceImpl(context: Context?, retrofitResponseListener: RetrofitResponseListener?) :
    BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(TestService::class.java)

    fun auth() {
        val call = service.auth()

        call.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                if (response.body() is Boolean) {
                    retrofitResponseListener.onRetrofitResponse(
                        RetrofitResponseObject(response.body()!!, TASK_ID)
                    )
                } else {
                    retrofitResponseListener.onRetrofitResponse(
                        RetrofitResponseObject(false, TASK_ID)
                    )
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }
        })
    }

    companion object {
        var TASK_ID = TestAuthServiceImpl::class.java.name.hashCode()
    }
}