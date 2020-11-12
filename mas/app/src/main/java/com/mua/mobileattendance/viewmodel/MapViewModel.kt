package com.mua.mobileattendance.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.mua.mobileattendance.retrofit.dto.CoordinateDto

class MapViewModel(application: Application) : BaseViewModel(application) {

    val editable = MutableLiveData<Boolean>(false)
    val latLngs: MutableLiveData<List<LatLng>> = MutableLiveData(ArrayList())

    fun latLngsToCoordinates(): ArrayList<CoordinateDto> {
        val res = ArrayList<CoordinateDto>()
        for (point in latLngs.value!!) {
            res.add(CoordinateDto(point))
        }
        return res
    }

    fun setLatLngs(coordinates: ArrayList<CoordinateDto>) {
        val res = ArrayList<LatLng>()
        coordinates.forEach {
            val tem = LatLng(it.lat, it.lon)
            res.add(tem)
        }
        latLngs.postValue(res)
    }

}