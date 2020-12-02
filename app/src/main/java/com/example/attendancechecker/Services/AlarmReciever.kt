package com.example.attendancechecker.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("ALARM","STARTED")
        val db = DB()
        val connection = db.getConnection()!!
        connection.autoCommit = false
        val st = connection.createStatement()
        val st1 = connection.createStatement()
        st.executeUpdate("UPDATE pupils SET leavedate = NULL")
        st1.executeUpdate("UPDATE pupils SET comedate = NULL")
        st.close()
        st1.close()
        connection.commit()
        connection.close()
    }
}