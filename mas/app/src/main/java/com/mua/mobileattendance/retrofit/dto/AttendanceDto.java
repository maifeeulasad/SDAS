package com.mua.mobileattendance.retrofit.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class AttendanceDto implements Serializable {
    @SerializedName("userId")
    public Long userId;
    @SerializedName("username")
    public String username;
    @SerializedName("attendanceId")
    public Long attendanceId;
    @SerializedName("coordinate")
    public CoordinateDto coordinate;
    @SerializedName("state")
    public StateDto state;
    @SerializedName("user")
    public UserDto user;
    @SerializedName("code")
    public String code;
    @SerializedName("session")
    public SessionDto session;
    @SerializedName("attendanceTime")
    public Date attendanceTime;
}
