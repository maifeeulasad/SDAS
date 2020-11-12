package com.mua.mobileattendance.retrofit.service

import com.mua.mobileattendance.retrofit.dto.ClassroomDto
import com.mua.mobileattendance.retrofit.dto.RoleDto
import com.mua.mobileattendance.retrofit.dto.SessionDto
import com.mua.mobileattendance.retrofit.dto.UserClassroomRoleDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ClassroomService {
    @GET("/classroom/all")
    fun all(): Call<List<ClassroomDto>>

    @GET("/classroom/all")
    fun all(@Query("query") keyword: String): Call<List<ClassroomDto>>

    @GET("/classroom/create")
    fun create(
        @Query("name") name: String,
        @Query("details") details: String,
        @Query("role") role: String
    ): Call<Long>

    @GET("/classroom/sessions")
    fun sessions(
        @Query("classroom_id") classroomId: Long
    ): Call<List<SessionDto>>

    @GET("/classroom/roles")
    fun roles(
        @Query("classroom_id") classroomId: Long
    ): Call<List<UserClassroomRoleDto>>

    @GET("/classroom/role")
    fun role(
        @Query("classroom_id") classroomId: Long
    ): Call<RoleDto>

    @GET("/classroom/join")
    fun join(
        @Query("classroom_id") classroomId: Long
    ): Call<Boolean>

    @POST("/classroom/edit")
    fun edit(
        @Query("classroom_id") classroomId: Long,
        @Body classroomDto: ClassroomDto
    ): Call<Boolean>


    @GET("/classroom/assign")
    fun assignUserId(
        @Query("classroom_id") classroomId: Long,
        @Query("user_id") userId: Long,
        @Query("role") role: RoleDto
    ): Call<Boolean>

    @GET("/classroom/assign")
    fun assignUsername(
        @Query("classroom_id") classroomId: Long,
        @Query("username") username: String,
        @Query("role") role: RoleDto
    ): Call<Boolean>


    @GET("/classroom/remove")
    fun removeUserId(
        @Query("classroom_id") classroomId: Long,
        @Query("user_id") userId: Long
    ): Call<Boolean>

    @GET("/classroom/remove")
    fun removeUsername(
        @Query("classroom_id") classroomId: Long,
        @Query("username") username: String
    ): Call<Boolean>

}