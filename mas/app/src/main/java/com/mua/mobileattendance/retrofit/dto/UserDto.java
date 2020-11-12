package com.mua.mobileattendance.retrofit.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDto implements Serializable {
    @SerializedName("userId")
    public Long userId;
    @SerializedName("username")
    public String username;
    @SerializedName("joiningDate")
    public Date joiningDate;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("contacts")
    public List<String> contacts = new ArrayList<>();
    @SerializedName("loginCredential")
    public LoginCredentialDto loginCredential;
    @SerializedName("userClassroomRoles")
    public List<UserClassroomRoleDto> userClassroomRoles = new ArrayList<>();
    @SerializedName("attendances")
    public List<AttendanceDto> attendances = new ArrayList<>();

}
