package com.mua.mobileattendance.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
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
import com.mua.mobileattendance.manager.LocationManager.DEFAULT_LOCATION as DEFAULT_LOCATION
import com.mua.mobileattendance.manager.LocationManager.ZOOM_LEVEL as ZOOM_LEVEL
import com.mua.mobileattendance.retrofit.dto.CoordinateDto
import com.mua.mobileattendance.tem.dto.CoordinateDtoTem
import com.mua.mobileattendance.viewmodel.MapViewModel


class MapActivity : BaseActivity(), OnMapReadyCallback,android.location.LocationListener {

    lateinit var mapFragment: SupportMapFragment
    private lateinit var mBinding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var map: GoogleMap
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var polygon: Polygon
    private var editable = false
    private var currentLocation = DEFAULT_LOCATION
    lateinit var mLocationRequest: LocationRequest
    lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    lateinit var context: Context
    lateinit var mLocationManager : LocationManager

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

        context = this

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                for (location in locationResult.locations) {
                    processLocation(location)
                }
            }
        }

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
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationManager
            .requestLocationUpdates(LocationManager.GPS_PROVIDER,
                500,
                1f,
                this)

        mLocationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(500)
            .setSmallestDisplacement(10f)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationResult = mFusedLocationProviderClient.lastLocation


        locationResult.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("d--mua","location fetch success")
                val lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    Log.d("d--mua","location is NOT null")
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
                                    ), ZOOM_LEVEL
                            )
                    )
                    drawPolygon()
                }else{
                    Log.d("d--mua","location is null")
                }
            } else {
                Log.d("d--mua","location fetch NOT success")
                Log.d("d--mua","location ---default")
                map.animateCamera(
                        CameraUpdateFactory
                                .newLatLngZoom(DEFAULT_LOCATION, ZOOM_LEVEL)
                )
            }
        }
        map.isMyLocationEnabled = true
        map.isBuildingsEnabled = true
        map.isIndoorEnabled = true
        map.isTrafficEnabled = false
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        setMap()
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

    fun processLocation(p0: Location){
        Log.d("d--mua","location "+p0.latitude + "," + p0.longitude)
        Log.d("d--mua","location "+p0.accuracy)
        if (!editable) {
            currentLocation =
                LatLng(p0.latitude, p0.longitude)
        }
        if (!editable && currentLocation != null) {
            Log.d("d--mua","added marker to location")
            map.addMarker(MarkerOptions().position(currentLocation))
        }
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    p0.latitude,
                    p0.longitude
                ), ZOOM_LEVEL
            )
        )
        drawPolygon()
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("d--mua","----------------------")
        processLocation(p0)
        Log.d("d--mua","----------------------")
    }

}