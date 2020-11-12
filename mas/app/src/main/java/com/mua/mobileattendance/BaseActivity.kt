package com.mua.mobileattendance

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.mua.mobileattendance.databinding.ActivityBaseBinding
import com.mua.mobileattendance.viewmodel.BaseViewModel
import com.mua.mobileattendance.viewmodel.Status

open class BaseActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var mBinding: ActivityBaseBinding
    private lateinit var baseViewModel: BaseViewModel
    private lateinit var progress: ProgressDialog
    private var minimizable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseInit()

        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        //setMinimizable(false)
    }


    fun baseInit() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        mBinding.base = baseViewModel
        mBinding.lifecycleOwner = this
    }


    private fun baseMessageInit() {
        baseViewModel.message.observe(this, Observer {
            while (it.size != 0) {
                val message = it.poll()
                try {
                    Snackbar.make(window.decorView.rootView, message, 2*1000)
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    private fun baseErrorInit() {
        baseViewModel.errorMessage.observe(this, Observer {
            while (it.size != 0) {
                val errorMessage = it.poll()
                try {
                    Snackbar.make(window.decorView.rootView, errorMessage, 2*1000)
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun initFromChild(child: BaseViewModel) {
        baseViewModel = child

        baseErrorInit()
        baseMessageInit()
        baseStatusInit()
    }

    private fun baseStatusInit() {
        baseViewModel.status.observe(this, Observer {
            if (it == Status.Loading) {
                progress = ProgressDialog.show(this, "", "Loading ...", true)
            } else if (::progress.isInitialized) {
                progress.cancel()
            }
        })
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    fun startActivityAndClear(packageContext: Context, cls: Class<out Any>) {
        val intent = Intent(packageContext, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun startActivity(packageContext: Context, cls: Class<out Any>) {
        val intent = Intent(packageContext, cls)
        startActivity(intent)
    }

    /*
    fun setFullScreen(){
        getWindow()
            .setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        getSupportActionBar()?.hide()
    }
     */


    fun setFullScreen() {
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun setSecureScreen() {
        if(!BuildConfig.DEBUG){
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (!minimizable) {
            //Toast.makeText(applicationContext, "Alert!\nRestart", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!minimizable) {
            //Toast.makeText(applicationContext, "Alert!\nDestroy", Toast.LENGTH_LONG).show()
        }
    }

    fun setMinimizable(minimizable: Boolean) {
        this.minimizable = minimizable
    }

    override fun onResume() {
        super.onResume()
        if (!minimizable) {
            //Toast.makeText(applicationContext, "Alert!\nResume", Toast.LENGTH_LONG).show()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!minimizable) {
            //Toast.makeText(applicationContext, "Alert!\nLeave", Toast.LENGTH_LONG).show()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onAppBackgrounded() {
        //Toast.makeText(applicationContext, "Alert!\nStop", Toast.LENGTH_LONG).show()
        if (!minimizable) {
            Toast.makeText(applicationContext, "Alert!\nStop", Toast.LENGTH_LONG).show()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onAppForegrounded() {
        //Toast.makeText(applicationContext, "Alert\nStart", Toast.LENGTH_LONG).show()
        if (!minimizable) {
            //Toast.makeText(applicationContext, "Alert\nStart", Toast.LENGTH_LONG).show()
        }
    }

}