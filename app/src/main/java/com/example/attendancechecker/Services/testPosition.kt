package com.example.attendancechecker.Services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.attendancechecker.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class testPosition : Service() {

    lateinit var geofencingClient: GeofencingClient
    var geofenceList : ArrayList<Geofence> = ArrayList()
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    override fun onCreate() {
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceList.add(
            Geofence.Builder()
            .setRequestId(123.toString())
            .setCircularRegion(53.704020, 23.815656, 100F)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build())
        //53.704020, 23.815656 College
        //53.658515, 23.842894 Home

        geofencingClient?.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d("GEO", "Geofences added")
            }
            addOnFailureListener {
                Log.d("GEO", "Failed to add geofences")
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createForeground()
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
        startForeground(124, notification)
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
        Log.d("GEO", "onDestroy() service")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
