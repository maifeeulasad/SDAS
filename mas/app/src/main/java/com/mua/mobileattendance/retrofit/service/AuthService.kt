package com.mua.mobileattendance.retrofit.service

import com.mua.mobileattendance.retrofit.dto.LoginCredentialDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthService {
    @GET("/auth/check")
    fun check(@Query("username") username: String): Call<Boolean>

    @POST("/auth/join")
    fun join(@Body loginCredentialDto: LoginCredentialDto): Call<Boolean>

    @POST("/auth/login")
    fun login(@Body loginCredentialDto: LoginCredentialDto): Call<LoginCredentialDto>
}