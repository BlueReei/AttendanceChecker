package com.example.attendancechecker.Services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.attendancechecker.R


class PositionService : Service() {
    val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private object locationListener : LocationListener {

        fun GenerateId() : String {
            val uniquePseudoID = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
            return uniquePseudoID
        }

        override fun onLocationChanged(location: Location?) {
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
           // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.toFloat(), locationListener)
       // Log.d("ASD", "onStartCommand()")
        createForeground()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1.toFloat(), locationListener)
        Log.d("ASD", "onStartCommand() finish")
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
        val proximityIntent = PendingIntent.getService(applicationContext, 123, Intent(this, WriteDate::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
        locationManager.addProximityAlert(53.658482, 23.843089, 50f, 10, proximityIntent)
        Log.d("ASD", "onStartCommand() finish proximity")
//       53.704299, 23.815838 stopSelf() 53.658482, 23.843089
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
