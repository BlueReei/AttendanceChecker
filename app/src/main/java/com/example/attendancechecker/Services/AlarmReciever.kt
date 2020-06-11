package com.example.attendancechecker.Services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReciever : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val db = DB()
        val connection = db.getConnection()!!
        connection.autoCommit = false
        val st = connection.createStatement()
        val st1 = connection.createStatement()
        st.executeUpdate("UPDATE pupils SET leavedate = NULL WHERE leavedate != NULL")
        st1.executeUpdate("UPDATE pupils SET comedate = NULL WHERE comedate != NULL")
        st.close()
        st1.close()
        connection.commit()
        connection.close()
    }
}