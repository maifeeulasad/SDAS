package com.mua.mobileattendance

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import com.mua.mobileattendance.activity.AuthActivity
import com.mua.mobileattendance.activity.HomeActivity
import com.mua.mobileattendance.helper.PermissionHelper
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.manager.SystemManager
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseListener
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseObject
import com.mua.mobileattendance.retrofit.service.impl.TestAuthServiceImpl
import java.lang.Thread.sleep

class MainActivity : BaseActivity(), RetrofitResponseListener {

    private var authenticated = false
    private var permissionGranted = false
    private var permissionPending = true
    private lateinit var testService: TestAuthServiceImpl
    private lateinit var context: Context

    //private var next = false

    //var permissionContactsRead = PermissionHelper(this@MainActivity, Manifest.permission.READ_CONTACTS)
    var permissionLocationFine =
        PermissionHelper(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        initLoadingWebView()

        //permissionContactsRead.request()
        permissionLocationFine.request()


        init()
    }

    fun initLoadingWebView() {
        val webView : WebView = findViewById(R.id.wv_load)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/load.gif")
    }

    private fun preProcess() {
        testService.auth()
    }

    private fun init() {
        testService = TestAuthServiceImpl(applicationContext, this)
        preProcess()
    }

    private fun startAuth() {
        startActivityAndClear(applicationContext, AuthActivity::class.java)
    }

    private fun startHome() {
        startActivityAndClear(applicationContext, HomeActivity::class.java)
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == TestAuthServiceImpl.TASK_ID) {
            authenticated = retrofitResponseObject.`object` as Boolean
            onAuth()
        } else if (retrofitResponseObject.requestCode == ServiceManager.ERROR_TASK_ID) {
            showErrorAndExit(
                "Failed to connect to server",
                SystemManager.INIT_NETWORK_CONNECTION_FAILED
            )
        }
    }

    fun onAuth() {
        if (authenticated && permissionGranted) {
            startHome()
        } else {
            if (!permissionPending) {
                startAuth()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissionLocationFine.onRequestPermissionResult(requestCode, grantResults) {
            onGranted {
                permissionGranted = true
                permissionPending = false
                onAuth()
            }
            onDenied { isPermanent ->
                if (isPermanent) {
                    showErrorAndExit(
                        "Without these permissions there is no use of SDAS",
                        SystemManager.INIT_PERMISSION_FAILED
                    )
                } else {
                    showErrorAndExit(
                        "Restart SDAS and grant permissions",
                        SystemManager.INIT_PERMISSION_FAILED
                    )
                }
            }
        }
    }



}
/*
    connect android
    adb start
    adb tcpip 5555
    //adb shell "ip addr show wlan0 | grep -e wlan0$ | cut -d\" \" -f 6 | cut -d/ -f 1"
    adb connect ip_mobile:5555
    */