package com.mua.mobileattendance.retrofit.service

import com.mua.mobileattendance.retrofit.dto.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface UserService {
    @GET("/user/details")
    fun getOne(@Query("user_id") userId: Long?): Call<UserDto>

    @GET("/user/details")
    fun getCurrent(): Call<UserDto>

    @GET("/user/edit")
    fun edit(@Query("name") name: String, @Query("email") email: String): Call<Boolean>

    @GET("/user/validate/same")
    fun isSameUser(@Query("user_id") userId: Long): Call<Boolean>
}