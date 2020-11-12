package com.mua.mobileattendance.fragment

import android.app.Fragment
import android.content.Context
import android.content.Intent

open class BaseFragment : Fragment() {

    fun startActivity(packageContext: Context, cls: Class<out Any>) {
        val intent = Intent(packageContext, cls)
        startActivity(intent)
    }

}