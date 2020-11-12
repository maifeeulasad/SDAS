package com.mua.mobileattendance.retrofit.service

import com.mua.mobileattendance.retrofit.dto.AttendanceDto
import com.mua.mobileattendance.retrofit.dto.StateDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface AttendanceService {
    @GET("/attendance/all")
    fun all(@Query("session_id") sessionId: Long?): Call<List<AttendanceDto>>

    @POST("/attendance/attend")
    fun attend(
        @Query("session_id") sessionId: Long?,
        @Body attendanceDto: AttendanceDto
    ): Call<Boolean>

    @GET("/attendance/verify")
    fun verify(
        @Query("session_id") sessionId: Long?,
        @Query("attendance_id") attendanceId: Long?,
        @Query("state") state: StateDto?
    ): Call<Boolean>
}