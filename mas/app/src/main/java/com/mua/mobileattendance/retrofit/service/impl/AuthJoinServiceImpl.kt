package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.LoginCredentialDto
import com.mua.mobileattendance.retrofit.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthJoinServiceImpl(context: Context?, retrofitResponseListener: RetrofitResponseListener?) :
    BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(AuthService::class.java)
    fun join(username: String?, password: String?) {
        join(LoginCredentialDto(username, password))
    }

    private fun join(loginCredentialDto: LoginCredentialDto) {
        val call = service.join(loginCredentialDto)

        call.enqueue(object : Callback<Boolean?> {
            override fun onResponse(call: Call<Boolean?>, response: Response<Boolean?>) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }
        })
    }

    companion object {
        var TASK_ID = AuthJoinServiceImpl::class.java.name.hashCode()
    }
}