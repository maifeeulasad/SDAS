package com.mua.mobileattendance.retrofit.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassroomDto implements Serializable {

    @SerializedName("classroomId")
    public Long classroomId;
    @SerializedName("name")
    public String name;
    @SerializedName("details")
    public String details;
    @SerializedName("userClassroomRoleList")
    public List<UserClassroomRoleDto> userClassroomRoleList = new ArrayList<>();
    @SerializedName("sessions")
    public List<SessionDto> sessions = new ArrayList<>();
    @SerializedName("nextSessionTime")
    public Date nextSessionTime;

}
