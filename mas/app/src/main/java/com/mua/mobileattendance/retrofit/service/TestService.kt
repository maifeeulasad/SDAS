package com.mua.mobileattendance.retrofit.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface TestService {
    @GET("/test/time")
    fun time(@Query("auth") auth: Boolean): Call<String>

    @GET("/test/auth")
    fun auth(): Call<Any>
}