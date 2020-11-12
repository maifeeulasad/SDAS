package com.mua.mobileattendance.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mua.mobileattendance.retrofit.dto.ClassroomDto
import com.mua.mobileattendance.retrofit.service.impl.ClassroomAllQueryServiceImpl
import com.mua.mobileattendance.retrofit.service.impl.ClassroomAllServiceImpl
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseListener
import com.mua.mobileattendance.retrofit.service.impl.RetrofitResponseObject
import java.util.*


open class HomeViewModel(application: Application) :
    BaseViewModel(application), RetrofitResponseListener {

    private var allClassroomService: ClassroomAllServiceImpl =
        ClassroomAllServiceImpl(application.baseContext, this)
    private var allQueryClassroomService: ClassroomAllQueryServiceImpl =
        ClassroomAllQueryServiceImpl(application.baseContext, this)

    var loading = MutableLiveData<Boolean>(false)
    val fabExpanded = MutableLiveData<Boolean>(false)
    var emptyHome = MutableLiveData<Boolean>(false)
    val searching = MutableLiveData<Boolean>(false)

    var resultantClassroomDtos = MutableLiveData<List<ClassroomDto>>(ArrayList())
    var classroomDtos = MutableLiveData<List<ClassroomDto>>(ArrayList())
    var searchedClassroomDtos = MutableLiveData<List<ClassroomDto>>(ArrayList())

    init {
        loadAllClassroom()
    }

    fun toggleFab() {
        fabExpanded.postValue(!fabExpanded.value!!)
    }

    fun loadAllClassroom() {
        loading.postValue(true)
        allClassroomService.all()
    }

    override fun onRetrofitResponse(retrofitResponseObject: RetrofitResponseObject?) {
        if (retrofitResponseObject!!.requestCode == ClassroomAllServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is List<*>) {
                val temClassroomDtos = retrofitResponseObject.`object` as List<ClassroomDto>
                emptyHome.postValue(temClassroomDtos.isEmpty())
                classroomDtos.postValue(temClassroomDtos)
                resultantClassroomDtos.postValue(temClassroomDtos)
                loading.postValue(false)
            }
        } else if (retrofitResponseObject.requestCode == ClassroomAllQueryServiceImpl.TASK_ID) {
            if (retrofitResponseObject.`object` is List<*>) {
                val temClassroomDtos = retrofitResponseObject.`object` as List<ClassroomDto>
                if (temClassroomDtos.size != 0) {
                    emptyHome.postValue(false)
                }
                searchedClassroomDtos.postValue(temClassroomDtos)
                loading.postValue(false)
            }
        }
    }

    fun allSearch(keyword: String) {
        searching.postValue(true)
        allQueryClassroomService.all(keyword)
    }

    fun findByName(keyword: String) {
        if (keyword == "") {
            resultantClassroomDtos.postValue(classroomDtos.value)
        }
        allSearch(keyword)
        val res = ArrayList<ClassroomDto>()
        classroomDtos.value!!.forEach {
            if (it.name != null && (it.name.contains(keyword) || it.details.contains(keyword))) {
                res.add(it)
            }
        }
        resultantClassroomDtos.postValue(res)
    }

}