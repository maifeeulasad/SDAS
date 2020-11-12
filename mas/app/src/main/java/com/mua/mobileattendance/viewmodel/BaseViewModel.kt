package com.mua.mobileattendance.viewmodel;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val message: MutableLiveData<Queue<String>> = MutableLiveData(LinkedList())

    val errorMessage: MutableLiveData<Queue<String>> = MutableLiveData(LinkedList())

    val status = MutableLiveData(Status.Loaded)

    fun addErrorMessage(errorMessage: String) {
        val errorMessages = this.errorMessage.value
        errorMessages!!.add(errorMessage)
        this.errorMessage.postValue(errorMessages)
    }

    fun addMessage(message: String) {
        val messages = this.message.value
        messages!!.add(message)
        this.message.postValue(messages)
    }
}

public enum class Status {
    Loading,
    Loaded
}