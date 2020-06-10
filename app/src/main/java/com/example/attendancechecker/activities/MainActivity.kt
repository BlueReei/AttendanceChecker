package com.example.attendancechecker.activities

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.attendancechecker.R
import com.example.attendancechecker.Services.AlarmReciever
import com.example.attendancechecker.Services.DB
import com.example.attendancechecker.Services.testPosition
import com.example.attendancechecker.presenters.MainPresenter
import com.example.attendancechecker.views.MainView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mcpv_circural_bar: CircularProgressView
    private lateinit var mtxt_hello_user: TextView
    lateinit var databasePupils : DatabaseReference

    @InjectPresenter
    lateinit var mainPresenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mtxt_hello_user = findViewById(R.id.txt_hello_user)
        mcpv_circural_bar = findViewById(R.id.cpv_circural_bar)
        databasePupils = FirebaseDatabase.getInstance().getReference("Pupils")

        val mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReciever::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 10
        calendar[Calendar.MINUTE] = 26
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        StartLoading()
        DeviceLogin()
        if (checkLocationPermission()) {
            val service = Intent(this, testPosition::class.java)
            Log.d("ASD", "startService()")
            startService(service)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 123)
            var grantResults = IntArray(0)
            onRequestPermissionsResult(123, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),grantResults = grantResults)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == 123 && grantResults.size == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val service = Intent(this, testPosition::class.java)
                Log.d("ASD", "startService() onRequestPermissions")
                startService(service)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun GenerateId() : String {
        val uniquePseudoID = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        return uniquePseudoID
    }

    override fun StartLoading() {
        mtxt_hello_user.visibility = View.GONE
        mcpv_circural_bar.visibility = View.VISIBLE
    }

    override fun EndLoading() {
        mcpv_circural_bar.visibility = View.GONE
        mtxt_hello_user.visibility = View.VISIBLE
    }

    override fun DeviceLogin() {
        val db = DB()
        val connection: Connection = db.getConnection()!!
        var hashlist : ArrayList<Int?> = ArrayList()
        val st: Statement = connection.createStatement()
        var rs: ResultSet
        st.use { st ->
            rs = st.executeQuery("SELECT hashcode FROM Pupils")
            while (rs.next()) {
                var hashcode = rs.getInt(db.HASHCODE)
                hashlist.add(hashcode)
            }
        }

        if (!hashlist.contains(GenerateId().hashCode())) {
            EndLoading()
            st.close()
            connection.close()
            mainPresenter.login(isSuccess = false)
        }
        else  {
            val st1: Statement = connection.createStatement()
            st1.use { st ->
                rs = st.executeQuery("SELECT name, surname FROM pupils WHERE Hashcode = ${GenerateId().hashCode()};")
                while (rs.next()) {
                    mtxt_hello_user.text = "Здравствуй ${rs.getString(db.SURNAME)} ${rs.getString(db.NAME)}!"
                }
            }
            st1.close()
            connection.close()
            mainPresenter.login(isSuccess = true)
        }
    }

    override fun ShowError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun OpenPupilList() {
        startActivity(Intent(applicationContext, PupilListActivity::class.java))
    }

    private fun checkLocationPermission(): Boolean {
        val p1 = ContextCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val p2 = ContextCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return p1 && p2
    }

    private fun requestLocationPermission() {
    }
}
