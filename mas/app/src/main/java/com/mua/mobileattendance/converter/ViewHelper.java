package com.mua.mobileattendance.converter;

import androidx.databinding.InverseMethod;

public class ViewHelper {

    //todo : refactor and move this file in adapter section

    @InverseMethod("stringToLong")
    public static String longToString(Long longValue) {
        if (longValue == null)
            return "";
        return longValue.toString();
    }

    public static Long stringToLong(String longString) {
        try {
            return Long.parseLong(longString);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static String selectOrViewBoundary(Boolean editing) {
        if (editing == null)
            return "Select boundary";
        return (editing ? "Select" : "View") + " boundary";
    }

    public static String createOrAttendSession(Boolean editing) {
        if (editing == null)
            return "Create session";
        return (editing ? "Create" : "Attend") + " session";
    }

}