package com.mua.mobileattendance.retrofit.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginCredentialDto implements Serializable {
    @SerializedName("key")
    public String key;
    @SerializedName("loginCredentialId")
    private Long loginCredentialId;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    //public String role="ROLE_USER";
    @SerializedName("user")
    private UserDto user;

    public LoginCredentialDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
