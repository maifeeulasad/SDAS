package com.mua.mobileattendance.retrofit.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserClassroomRoleDto implements Serializable {

    @SerializedName("userClassroomRoleId")
    public Long userClassroomRoleId;
    @SerializedName("userId")
    public Long userId;
    @SerializedName("username")
    public String username;
    @SerializedName("role")
    public RoleDto role;


}
