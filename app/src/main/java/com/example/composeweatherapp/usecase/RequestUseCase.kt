package com.example.composeweatherapp.usecase

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.composeweatherapp.MainActivity
import com.example.composeweatherapp.core.roundTo
import com.example.composeweatherapp.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.ref.WeakReference
import javax.inject.Inject

class RequestUseCase @Inject constructor(
    private val weatherRepo: WeatherRepository
) {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var _context: WeakReference<Context>? = null

    private var _activity: WeakReference<MainActivity>? = null

    fun subscribe(activity: MainActivity, context: Context) {
        _activity = WeakReference(activity)
        _context = WeakReference(context)
    }

    fun getLocation() {
        fusedLocationProviderClient = _activity?.get()
            ?.let { LocationServices.getFusedLocationProviderClient(it) }
        if (_context?.get()?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && _context?.get()?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            locationRequest()
        } else {
            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
                if (location != null) {
                    weatherRepo.locationLat = location.latitude.roundTo(1).toString()
                    weatherRepo.locationLon = location.longitude.roundTo(1).toString()
                } else {
                    weatherRepo.locationLat = "no_location"
                    weatherRepo.locationLon = "no_location"
                }
            }
        }
    }

    private fun locationRequest() {
        _activity?.get()?.let { activity ->
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }
}