package com.mua.mobileattendance.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.mua.mobileattendance.BaseActivity
import com.mua.mobileattendance.R
import com.mua.mobileattendance.databinding.ActivityMapBinding
import com.mua.mobileattendance.manager.ActivityManager
import com.mua.mobileattendance.manager.LocationManager
import com.mua.mobileattendance.retrofit.dto.CoordinateDto
import com.mua.mobileattendance.tem.dto.CoordinateDtoTem
import com.mua.mobileattendance.viewmodel.MapViewModel


class MapActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var mapFragment: SupportMapFragment
    private lateinit var mBinding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var map: GoogleMap
    private lateinit var polygon: Polygon
    private var editable = false
    private var currentLocation = LocationManager.DEFAULT_LOCATION
    lateinit var mLocationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        try {
            editable = intent.extras!!.getBoolean("editable")
            viewModel.editable.postValue(editable)
        } catch (e: Exception) {
        }
    }


    fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        mBinding.map = viewModel
        mBinding.lifecycleOwner = this



        initFromChild(viewModel)

        fetchLocationAsync()
        initMapButtons()
        initDoneButton()

        fetchLocations()

        setMinimizable(true)
    }

    fun fetchLocations() {
        try {
            val points = intent.extras!!.getParcelableArrayList<CoordinateDto>("locations")
            viewModel.setLatLngs(points as ArrayList<CoordinateDto>)
        } catch (e: Exception) {
        }
    }

    fun initDoneButton() {
        mBinding.ibDone.setOnClickListener {
            intent.putParcelableArrayListExtra(
                "locations",
                viewModel.latLngsToCoordinates()
            )
            intent.putExtra("location", CoordinateDtoTem(currentLocation))
            setResult(ActivityManager.MAP_ON_SUCCESS_RESULT_CODE, intent)
            finish()
        }
    }

    fun fetchLocationAsync() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.smf_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    fun drawPolygon() {
        try {
            val polygonNew =
                map.addPolygon(
                    PolygonOptions().add(*(viewModel.latLngs.value!!.toTypedArray()))
                        .strokeColor(Color.GREEN)
                        .fillColor(Color.RED)
                )
            try {
                polygon.remove()
            } catch (e: Exception) {

            }
            polygon = polygonNew
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initMapButtons() {
        mBinding.ibClear.setOnClickListener {
            map.clear()
            viewModel.latLngs.value = ArrayList()
        }
    }

    @SuppressLint("MissingPermission")
    fun setMap() {
        mLocationRequest = LocationRequest()
        /*

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2*1000
        mLocationRequest.fastestInterval = 2*1000

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)


        val mFusedLocationProviderClient
                = LocationServices
            .getFusedLocationProviderClient(this)
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                val last = p0!!.lastLocation
                currentLocation = LatLng(last.latitude,last.longitude)
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }
        }, Looper.getMainLooper())
        */

        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        val locationResult = mFusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    if (!editable) {
                        currentLocation =
                            LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                    }
                    if (!editable && currentLocation != null) {
                        map.addMarker(MarkerOptions().position(currentLocation))
                    }
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude
                            ), LocationManager.ZOOM_LEVEL
                        )
                    )
                    drawPolygon()
                }
            } else {
                map.animateCamera(
                    CameraUpdateFactory
                        .newLatLngZoom(LocationManager.DEFAULT_LOCATION, LocationManager.ZOOM_LEVEL)
                )
            }
        }
        map.uiSettings.isMyLocationButtonEnabled = true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        setMap()
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isCompassEnabled = true
        googleMap.setOnMapClickListener { latlng ->
            googleMap.addMarker(MarkerOptions().position(latlng))
            val points = viewModel.latLngs.value?.toCollection(ArrayList())
            points?.add(latlng)
            viewModel.latLngs.value = points
            if (editable) {
                if (points?.size!! > 2) {
                    drawPolygon()
                }
            } else {
                if (currentLocation != null) {
                    map.addMarker(MarkerOptions().position(currentLocation))
                }
            }
        }
    }
}