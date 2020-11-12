package com.mua.mobileattendance.tem.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.mua.mobileattendance.retrofit.dto.CoordinateDto

class CoordinateDtoTem(private val lat: Double, private val lon: Double) : Parcelable {

    constructor(latLng: LatLng) : this(latLng.latitude, latLng.longitude) {}

    fun toCoordinateDto(): CoordinateDto {
        return CoordinateDto(lat, lon)
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }


    companion object {
        @JvmField
        var CREATOR: Parcelable.Creator<CoordinateDto?> =
            object : Parcelable.Creator<CoordinateDto?> {
                override fun createFromParcel(source: Parcel): CoordinateDto? {
                    return CoordinateDto(source)
                }

                override fun newArray(size: Int): Array<CoordinateDto?> {
                    return arrayOfNulls(size)
                }
            }
    }


}