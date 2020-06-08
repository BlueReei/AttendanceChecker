package com.example.attendancechecker.Services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class WriteDate : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //val db by lazy { baseContext.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }
        Log.d("ProximityService", "1")
        val status = intent!!.extras.get("KEY_PROXIMITY_ENTERING")
        if (status == true) { Log.d("ProximityService", "Entering") } else { Log.d("ProximityService", "Leaving") }
        stopSelf()
        Log.d("ProximityService", "2")
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
