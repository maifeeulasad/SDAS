package com.mua.mobileattendance.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mua.mobileattendance.manager.AuthenticationManager
import com.mua.mobileattendance.manager.ServiceManager
import com.mua.mobileattendance.retrofit.dto.LoginCredentialDto
import com.mua.mobileattendance.retrofit.service.impl.*

open class AuthViewModel(application: Application) :
    BaseViewModel(application), RetrofitResponseListener {
    private var loginAuthService: AuthLoginServiceImpl =
        AuthLoginServiceImpl(application.baseContext, this)
    private var joinAuthService: AuthJoinServiceImpl =
        AuthJoinServiceImpl(application.baseContext, this)
    private var checkAuthService: AuthCheckServiceImpl =
        AuthCheckServiceImpl(application.baseContext, this)

    val username = MutableLiveData("")
    val usernameError = MutableLiveData("")
    val password = MutableLiveData("")
    val rePassword = MutableLiveData("")
    val tabIndex = MutableLiveData(1)

    val applicationMain = application
    val startHome = MutableLiveData(false)

    val rePasswordError = MediatorLiveData<String>()

    val canSignup = MediatorLiveData<Boolean>()
    val canLogin = MediatorLiveData<Boolean>()

    init {
        canSignup.postValue(false)
        canLogin.postValue(false)
        canSignup.addSource(password) {
            canSignup
                .postValue(
                    password.value == rePassword.value
                            && password.value != ""
                            && username.value != ""
                )
            rePasswordError
                .postValue(
                    if (password.value == rePassword.value) ""
                    else "Please enter same password to continue"
                )
        }
        canSignup.addSource(rePassword) {
            canSignup.postValue(
                password.value == rePassword.value
                        && password.value != ""
                        && username.value != ""
            )
            rePasswordError
                .postValue(
                    if (password.value == rePassword.value) ""
                    else "Please enter same password to continue"
                )
        }
        canSignup.addSource(username) {
            if (it != "")
                checkAuthService.check(username.value!!)
            usernameError.postValue("")
            canSignup.postValue(
                password.value == rePassword.value
                        && password.value != ""
                        && username.value != ""
            )
        }
        canLogin.addSource(username) {
            canLogin.postValue(username.value != "" && password.value != "")
        }
        canLogin.addSource(password) {
            canLogin.postValue(username.value != "" && password.value != "")
        }
    }

    fun join(): View.OnClickListener {
        return View.OnClickListener {
            status.postValue(Status.Loading)
            joinAuthService.join(username.value, password.value)
        }
    }

    fun login(): View.OnClickListener {
        return View.OnClickListener {
            status.postValue(Status.Loading)
            loginAuthService.login(username.value, password.value)
        }
    }


    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        status.postValue(Status.Loaded)
        if (retrofitResponseObject!!.requestCode == AuthCheckServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is Boolean) {
                if (retrofitResponseObject.`object` == false) {
                    usernameError.postValue("Username \"${username.value}\" not available")
                    canSignup.postValue(false)
                } else {
                    usernameError.postValue("")
                    canSignup.postValue(true)
                }
            }
        } else if (retrofitResponseObject.requestCode == AuthLoginServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is LoginCredentialDto) {
                val loginCredentialDto = (retrofitResponseObject.`object` as LoginCredentialDto)
                addMessage("Login Successful")
                AuthenticationManager
                    .setAuthKey(applicationMain.baseContext, loginCredentialDto.key)
                startHome.postValue(true)
            }
        } else if (retrofitResponseObject.requestCode == AuthJoinServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is Boolean) {
                if (retrofitResponseObject.`object` == true) {
                    onJoinSuccess()
                } else {
                    addErrorMessage("Account creation failed !")
                }
            }
        } else if (retrofitResponseObject.requestCode == ServiceManager.ERROR_TASK_ID) {
            addErrorMessage("Some error encountered!\nPlease check again")
            //addErrorMessage(retrofitResponseObject.`object`.toString())
        }
    }

    fun onJoinSuccess() {
        tabIndex.postValue(0)
    }

}
