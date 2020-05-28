package com.example.attendancechecker.Services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class PositionService : Service() {

    private object locationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Log.i("Service", "${location.toString()}")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(p0: String?) {

        }

        override fun onProviderDisabled(p0: String?) {
            TODO("Not yet implemented")
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val runnable = Runnable { locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10*1000, 10.toFloat(), locationListener) }
        val thread = Thread(runnable)
        thread.start()
        return START_STICKY
    }
}
