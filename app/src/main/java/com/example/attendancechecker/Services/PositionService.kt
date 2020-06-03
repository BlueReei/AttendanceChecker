package com.example.attendancechecker.Services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.attendancechecker.R

class PositionService : Service() {
    val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private object locationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            if ((location!!.longitude > 20 && location!!.longitude < 30) && (location!!.latitude > 50 && location!!.latitude < 60)) { Log.e("Service", "true") }
            else  { Log.e("Service", "false") }
            //Log.e("Service", "${location!!.longitude} ${location!!.latitude}")
            Log.d("Service", "${location.toString()}")
            Log.d("ASD", "onLocationChanged()")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            Log.d("ASD", "onStatusChanged()")
        }

        override fun onProviderEnabled(p0: String?) {
            Log.d("ASD", "onProviderEnabled()")
        }

        override fun onProviderDisabled(p0: String?) {
            Log.d("ASD", "onProviderDisabled()")
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), locationListener)
        Log.d("ASD", "onStartCommand()")
        createForeground()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1.toFloat(), locationListener)
        Log.d("ASD", "onStartCommand() finish")
//        stopSelf()
        return START_STICKY
    }

    private fun createForeground() {

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("position_service", "My_great_geo_service")
        } else {
            "position_service"
        }

        val notification = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setCategory(Notification.CATEGORY_SERVICE)
        }.build()
        startForeground(123, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String): String {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Location service description"
            lightColor = Color.RED
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return id
    }

    override fun onDestroy() {
        Log.d("ASD", "onDestroy() service")
        super.onDestroy()
    }
}
