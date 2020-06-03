package com.example.attendancechecker.Services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class PositionService : Service() {
    val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private object locationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            if ((location!!.longitude > 20 && location!!.longitude < 30) && (location!!.latitude > 50 && location!!.latitude < 60)) { Log.e("Service", "true") }
            else  { Log.e("Service", "false") }
            //Log.e("Service", "${location!!.longitude} ${location!!.latitude}")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {

        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), locationListener)
        return START_STICKY
    }
}
