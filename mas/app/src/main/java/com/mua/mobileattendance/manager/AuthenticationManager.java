package com.mua.mobileattendance.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mua.mobileattendance.util.SharedPreferenceUtil;

public class AuthenticationManager {

    public static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_KEY = "AUTH_KEY";

    public static String getAuthKey(Context context) {
        return SharedPreferenceUtil.getString(context, AUTH_KEY, "");
    }

    public static void setAuthKey(Context context, @NonNull String authKey) {
        SharedPreferenceUtil.setString(context, AUTH_KEY, authKey);
    }

    public static Boolean removeAuthKey(Context context) {
        try {
            SharedPreferenceUtil.removeAll(context);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
