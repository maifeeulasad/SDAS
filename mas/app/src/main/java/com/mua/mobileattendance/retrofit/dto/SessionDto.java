package com.mua.mobileattendance.retrofit.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionDto implements Serializable {

    @SerializedName("sessionId")
    public Long sessionId;
    @SerializedName("classroomId")
    public Long classroomId;
    @SerializedName("weight")
    public Long weight;
    @SerializedName("minutes")
    public Long minutes;
    @SerializedName("code")
    public String code;
    @SerializedName("bounds")
    public List<CoordinateDto> bounds = new ArrayList<>();
    @SerializedName("classroom")
    public ClassroomDto classroom;
    @SerializedName("attendances")
    public List<AttendanceDto> attendances = new ArrayList<>();
    @SerializedName("creationTime")
    public Date creationTime;


    @Override
    public String toString() {
        return "SessionDto{" +
                "sessionId=" + sessionId +
                ", classroomId=" + classroomId +
                ", weight=" + weight +
                ", minutes=" + minutes +
                ", code='" + code + '\'' +
                ", bounds=" + bounds +
                ", classroom=" + classroom +
                ", attendances=" + attendances +
                ", creationTime=" + creationTime +
                '}';
    }
}
