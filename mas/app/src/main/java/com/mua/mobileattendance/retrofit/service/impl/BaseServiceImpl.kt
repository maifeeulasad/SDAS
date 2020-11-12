package com.mua.mobileattendance.retrofit.service.impl

import android.content.Context

open class BaseServiceImpl(
    var context: Context,
    var retrofitResponseListener: RetrofitResponseListener
)