package com.mua.mobileattendance.retrofit.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CoordinateDto : Parcelable, Serializable {

    @SerializedName("lat")
    var lat: Double

    @SerializedName("lon")
    var lon: Double

    constructor(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
    }

    constructor(parcel: Parcel) {
        lat = parcel.readDouble()
        lon = parcel.readDouble()
    }


    constructor(latLng: LatLng) : this(latLng.latitude, latLng.longitude)
    constructor(point: CoordinateDto) : this(point.lat, point.lon)

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

    override fun toString(): String {
        return "{" +
                "lat : " + lat +
                "lon : " + lon + "," +
                "}"
    }

}