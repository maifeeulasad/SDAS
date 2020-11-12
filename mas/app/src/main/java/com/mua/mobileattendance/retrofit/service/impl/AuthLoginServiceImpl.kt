package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context
import com.mua.mobileattendance.manager.RetrofitManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.LoginCredentialDto
import com.mua.mobileattendance.retrofit.service.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthLoginServiceImpl(context: Context?, retrofitResponseListener: RetrofitResponseListener?) :
    BaseServiceImpl(context!!, retrofitResponseListener!!) {
    var retrofit = RetrofitManager.newInstance(context)
    var service = retrofit.create(AuthService::class.java)
    fun login(username: String?, password: String?) {
        login(LoginCredentialDto(username, password))
    }

    private fun login(loginCredentialDto: LoginCredentialDto) {
        val call = service.login(loginCredentialDto)
        call.enqueue(object : Callback<LoginCredentialDto?> {
            override fun onResponse(
                call: Call<LoginCredentialDto?>,
                response: Response<LoginCredentialDto?>
            ) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(response.body()!!, TASK_ID)
                )
            }

            override fun onFailure(call: Call<LoginCredentialDto?>, t: Throwable) {
                retrofitResponseListener.onRetrofitResponse(
                    RetrofitResponseObject(t.message!!, ServiceManager.ERROR_TASK_ID)
                )
            }
        })
    }

    companion object {
        var TASK_ID = AuthLoginServiceImpl::class.java.name.hashCode()
    }
}