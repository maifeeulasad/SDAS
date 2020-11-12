package com.mua.mobileattendance.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.mua.mobileattendance.retrofit.dto.UserDto
import com.mua.mobileattendance.retrofit.service.impl.*


open class ProfileViewModel(application: Application) :
    BaseViewModel(application), RetrofitResponseListener {

    private var getOneUserService: UserGetOneServiceImpl =
        UserGetOneServiceImpl(application.baseContext, this)
    private var getCurrentUserService: UserGetCurrentServiceImpl =
        UserGetCurrentServiceImpl(application.baseContext, this)
    private var editUserService: UserEditServiceImpl =
        UserEditServiceImpl(application.baseContext, this)
    private var isSameUserUserService: UserIsSameUserServiceImpl =
        UserIsSameUserServiceImpl(application.baseContext, this)


    val userId = MutableLiveData<Long>(0)
    val user = MutableLiveData<UserDto>(UserDto())
    val editable = MutableLiveData<Boolean>(false)

    fun save(): View.OnClickListener {
        return View.OnClickListener {
            if (user.value!!.email != null) {
                editUserService.edit(user.value!!.name, user.value!!.email)
            }
        }
    }

    fun contacts(): String {
        var res = ""
        if (user.value!!.contacts.size == 0) {
            res = "No contact found!"
        } else {
            user.value!!.contacts.forEach {
                res += it + "\n"
            }
        }
        return res
    }


    fun setUserIdAndLoad(userId: Long) {
        this.userId.postValue(userId)
        getOneUserService.getOne(userId)
        isSameUserUserService.isSameUser(userId)
    }

    fun setUserIdAndLoad() {
        getCurrentUserService.getCurrent()
        editable.postValue(true)
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == UserGetOneServiceImpl.TASK_ID) {
            val tem = retrofitResponseObject.`object` as UserDto
            user.postValue(tem)
            userId.postValue(tem.userId)
        } else if (retrofitResponseObject.requestCode == UserEditServiceImpl.TASK_ID) {
            if(retrofitResponseObject.`object` as Boolean){
                addMessage("Profile Edit Successful")
            }else{
                addMessage("Profile Edit Failed")
            }
        } else if (retrofitResponseObject.requestCode == UserIsSameUserServiceImpl.TASK_ID) {
            addMessage("Just click Save to edit your profile")
            editable.postValue(retrofitResponseObject.`object` as Boolean)
        } else if (retrofitResponseObject.requestCode == UserGetCurrentServiceImpl.TASK_ID) {
            val tem = retrofitResponseObject.`object` as UserDto
            user.postValue(tem)
            userId.postValue(tem.userId)
        }
    }

}