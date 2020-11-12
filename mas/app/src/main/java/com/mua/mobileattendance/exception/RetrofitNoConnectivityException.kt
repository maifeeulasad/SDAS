package com.mua.mobileattendance.exception

import java.io.IOException

class RetrofitNoConnectivityException @JvmOverloads constructor(message: String? = "No Internet Connectivity. Please check your Wifi or Data connection.") :
    IOException(message)