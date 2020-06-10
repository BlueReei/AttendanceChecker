package com.example.attendancechecker.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import java.sql.Connection
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        //val db by lazy { context!!.applicationContext.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }
        if (geofencingEvent.hasError()) {
            Log.e("GEO_ERROR", "${GeofenceStatusCodes.ERROR}")
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            // Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences)

            // Send notification and log the transition details.
            Log.i("GEO", geofenceTransitionDetails)
        } else {
            // Log the error.
            Log.e("GEO", "geofence_transition_invalid_type")
        }
    }

    private fun getGeofenceTransitionDetails(
        geofenceTransition: Int,
        triggeringGeofences: List<Geofence>
    ): String {
        val geofenceTransitionString: String = getTransitionString(geofenceTransition)

        // Get the Ids of each geofence that was triggered.
        val triggeringGeofencesIdsList: ArrayList<String?> = ArrayList()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString =
            TextUtils.join(", ", triggeringGeofencesIdsList)
        return "$geofenceTransitionString: $triggeringGeofencesIdsString"
    }

    fun GenerateId() : String {
        val uniquePseudoID = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        return uniquePseudoID
    }

    private fun getTransitionString(transitionType: Int): String {
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                val db = DB()
                val connection = db.getConnection()!!
                connection.autoCommit = false
                val currentDate = Date()
                val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
                val timeText = timeFormat.format(currentDate)
                val st = connection.createStatement()
                val st1 = connection.createStatement()
                var comedate : String? = null
                st.use { st ->
                    var rs = st.executeQuery("SELECT comedate FROM pupils WHERE hashcode = ${GenerateId().hashCode()}")
                    while (rs.next()) {
                        comedate = rs.getString(db.COMEDATE)
                    }
                }
                if (timeText > comedate.toString() || comedate == null) st1.executeUpdate("UPDATE pupils SET comedate = $timeText WHERE hashcode = ${GenerateId().hashCode()}")
                st.close()
                st1.close()
                connection.commit()
                connection.close()
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val db = DB()
                val connection = db.getConnection()!!
                connection.autoCommit = false
                val currentDate = Date()
                val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
                val timeText = timeFormat.format(currentDate)
                val st = connection.createStatement()
                val st1 = connection.createStatement()
                var leavedate : String? = null
                st.use { st ->
                    var rs = st.executeQuery("SELECT leavedate FROM pupils WHERE hashcode = ${GenerateId().hashCode()}")
                    while (rs.next()) {
                        leavedate = rs.getString(db.LEAVEDATE)
                    }
                }
                if (timeText < leavedate.toString() || leavedate == null)  st1.executeUpdate("UPDATE pupils SET leavedate = $timeText WHERE hashcode = ${GenerateId().hashCode()}")
                st.close()
                st1.close()
                connection.commit()
                connection.close()
            }
        }
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "geofence_transition_entered"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "geofence_transition_exited"
            else -> "unknown_geofence_transition"
        }
    }

}
