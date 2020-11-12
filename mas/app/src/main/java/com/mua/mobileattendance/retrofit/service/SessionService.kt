package com.mua.mobileattendance.retrofit.service

import com.mua.mobileattendance.retrofit.dto.SessionDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface SessionService {
    @POST("/session/create")
    fun create(@Query("classroom_id") classroomId: Long, @Body session: SessionDto): Call<Long>
}