package com.mua.mobileattendance.retrofit.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum RoleDto {
    Teacher("Teacher"),
    Student("Student"),
    CR("CR"),
    Pending("Pending");

    @SerializedName("role")
    String role;

    RoleDto(String role) {
        this.role = role;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RoleDto fromValue(String role) {
        for (RoleDto e : values()) {
            if (e.role.equalsIgnoreCase(role)) {
                return e;
            }
        }
        return null;
    }

    @JsonValue
    public String getRole() {
        return role;
    }

}
