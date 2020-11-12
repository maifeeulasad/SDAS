package com.mua.mobileattendance.retrofit.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum StateDto {
    AutoAccepted("AutoAccepted"),
    AutoRejected("AutoRejected"),
    Accepted("Accepted"),
    Rejected("Rejected"),
    Pending("Pending");


    @SerializedName("state")
    String state;

    StateDto(String state) {
        this.state = state;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StateDto fromValue(String state) {
        for (StateDto e : values()) {
            if (e.state.equalsIgnoreCase(state)) {
                return e;
            }
        }
        return null;
    }

    @JsonValue
    public String getState() {
        return state;
    }
}
