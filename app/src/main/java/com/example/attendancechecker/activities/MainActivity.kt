package com.example.attendancechecker.activities

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
import jp.wasabeef.blurry.Blurry
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mcpv_circural_bar: CircularProgressView
    private lateinit var mtxt_hello_user: TextView
    private lateinit var mtxt_come: TextView
    private lateinit var mtxt_leave: TextView
    private lateinit var mtxt_come_date: TextView
    private lateinit var mtxt_leave_date: TextView
    private lateinit var rootView: FrameLayout

    @InjectPresenter
    lateinit var mainPresenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootView = findViewById(R.id.main_layout)
        mtxt_hello_user = findViewById(R.id.txt_hello_user)
        mcpv_circural_bar = findViewById(R.id.cpv_circural_bar)
        mtxt_come = findViewById(R.id.txt_come)
        mtxt_leave = findViewById(R.id.txt_leave)
        mtxt_come_date = findViewById(R.id.txt_come_date)
        mtxt_leave_date = findViewById(R.id.txt_leave_date)

        mtxt_come.visibility = View.GONE
        mtxt_come_date.visibility = View.GONE
        mtxt_leave.visibility = View.GONE
        mtxt_leave_date.visibility = View.GONE

        if (GenerateId().hashCode() == -32741541) {
            //Every day setting dates to null
            val mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReciever::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 11, intent, 0)
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 23
            calendar[Calendar.MINUTE] = 59
            mAlarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            //Email
            /*val db = DB()
            val connection = db.getConnection()!!
            connection.autoCommit = false
            val st = connection.createStatement()
            var pupilslatelist : ArrayList<String> = ArrayList()
            pupilslatelist.clear()
            val it = Intent(Intent.ACTION_SEND)
            st.use { st ->
                var rs = st.executeQuery("SELECT * FROM pupils WHERE comedate < '90000' ORDER BY groupname")
                while (rs.next()) {
                    pupilslatelist.add("${rs.getString(db.SURNAME)} ${rs.getString(db.NAME)} ${rs.getString(db.THIRDNAME)} - ${rs.getString(db.GROUP)} - ${rs.getString(db.COMEDATE)}")
                }
            }
            it.putExtra(Intent.EXTRA_EMAIL, arrayOf("lalki351@gmail.com"))
            it.putExtra(Intent.EXTRA_SUBJECT, "Список опоздавших")
            it.putExtra(Intent.EXTRA_TEXT, "$pupilslatelist")
            it.type = "message/rfc822"
            val itPendingIntent = PendingIntent.getActivity(this, 12, it, 0)
            calendar[Calendar.HOUR_OF_DAY] = 9
            calendar[Calendar.MINUTE] = 30
            st.close()
            connection.close()
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, itPendingIntent)*/
        }


        StartLoading()
        DeviceLogin()
        if (checkLocationPermission()) {
            val service = Intent(this, testPosition::class.java)
            rootView.background = WallpaperManager.getInstance(this).drawable
            Blurry.with(this).radius(25).sampling(2).onto(rootView)
            Log.d("ASD", "startService()")
            startService(service)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 123
            )
            var grantResults = IntArray(0)
            onRequestPermissionsResult(
                123, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), grantResults = grantResults
            )
        }
    }

    override fun onResume() {
        Log.d("MMM", "onResume")
        super.onResume()
    }

    override fun onRestart() {
        Log.d("MMM", "onRestart")
        super.onRestart()
        recreate()
    }

    override fun onStart() {
        Log.d("MMM", "onStart")
        super.onStart()
    }

    override fun onStop() {
        Log.d("MMM", "onStop")
        super.onStop()
    }

    override fun onPause() {
        Log.d("MMM", "onPause")
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 123 ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                val service = Intent(this, testPosition::class.java)
                rootView.background =  WallpaperManager.getInstance(this).drawable
                Blurry.with(this).radius(25).sampling(2).onto(rootView)
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
                rs = st.executeQuery("SELECT * FROM pupils WHERE Hashcode = ${GenerateId().hashCode()};")
                while (rs.next()) {
                    mtxt_hello_user.text = "Здравствуй\n${rs.getString(db.SURNAME)} ${rs.getString(
                        db.NAME
                    )}!"
                    if (rs.getString(db.COMEDATE) == null) {
                        mtxt_come.visibility = View.GONE
                        mtxt_come_date.visibility = View.GONE
                    }
                    if (rs.getString(db.LEAVEDATE) == null) {
                        mtxt_leave.visibility = View.GONE
                        mtxt_leave_date.visibility = View.GONE
                        }
                    rs.getString(db.COMEDATE)?.let {
                        mtxt_come.text = "Приход"
                        mtxt_come.visibility = View.VISIBLE
                        if (rs.getString(db.COMEDATE).length == 6) mtxt_come_date.text = "${rs.getString(
                            db.COMEDATE
                        ).substring(0, 2)}:${rs.getString(db.COMEDATE).substring(2, 4)}"
                        else mtxt_come_date.text = "${rs.getString(db.COMEDATE).substring(0, 1)}:${rs.getString(
                            db.COMEDATE
                        ).substring(1, 3)}"
                        mtxt_come_date.visibility = View.VISIBLE
                    }
                    rs.getString(db.LEAVEDATE)?.let {
                        mtxt_leave.text = "Уход"
                        mtxt_leave.visibility = View.VISIBLE
                        if (rs.getString(db.LEAVEDATE).length == 6) mtxt_leave_date.text = "${rs.getString(
                            db.LEAVEDATE
                        ).substring(0, 2)}:${rs.getString(db.LEAVEDATE).substring(2, 4)}"
                        else mtxt_leave_date.text = "${rs.getString(db.LEAVEDATE).substring(0, 1)}:${rs.getString(
                            db.LEAVEDATE
                        ).substring(1, 3)}"
                        mtxt_leave_date.visibility = View.VISIBLE
                    }
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
        if (mtxt_hello_user.visibility == View.VISIBLE) {
            startActivity(Intent(applicationContext, PupilListActivity::class.java))
        }
    }

    private fun checkLocationPermission(): Boolean {
        val p1 = ContextCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val p2 = ContextCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val p3 = ContextCompat
            .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return p1 && p2 && p3
    }
}