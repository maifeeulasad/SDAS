package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.service.TestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestTimeServiceImpl(context: Context?, retrofitResponseListener: RetrofitResponseListener?) :
    BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(TestService::class.java)

    fun time(auth: Boolean) {
        val call = service.time(auth)

        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.body() == null) {
                    retrofitResponseListener.onRetrofitResponse(
                        RetrofitResponseObject("", TASK_ID)
                    )
                } else {
                    retrofitResponseListener.onRetrofitResponse(
                        RetrofitResponseObject(response.body()!!, TASK_ID)
                    )
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }
        })
    }

    companion object {
        var TASK_ID = TestTimeServiceImpl::class.java.name.hashCode()
    }
}