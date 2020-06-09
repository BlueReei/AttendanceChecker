package com.example.attendancechecker.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.attendancechecker.R
import com.example.attendancechecker.Services.DB
import com.example.attendancechecker.Services.TestBase
import com.example.attendancechecker.Services.testPosition
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.MainPresenter
import com.example.attendancechecker.views.MainView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mcpv_circural_bar: CircularProgressView
    private lateinit var mtxt_hello_user: TextView
    val db by lazy { baseContext.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }
    lateinit var databasePupils : DatabaseReference


    @InjectPresenter
    lateinit var mainPresenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mtxt_hello_user = findViewById(R.id.txt_hello_user)
        mcpv_circural_bar = findViewById(R.id.cpv_circural_bar)
        databasePupils = FirebaseDatabase.getInstance().getReference("Pupils")

        val pupil1 = PupilModel(0, "https://static.mk.ru/upload/entities/2020/04/14/15/articles/detailPicture/f9/aa/3a/55/eb9f0dcbfe069ff7c772b31e88c4210b.jpg", "27тп", "Андрей", "Паска", "Сергеевич", -32741541)
        val pupil2 = PupilModel(1, null, "27тп", "Владислав", "Петров", "ХЗ", null)
        val pupil3 = PupilModel(2, null, "27тп", "Антон", "Юлбарисов", "ХЗ", null)
        databasePupils.child("${pupil1.id}").setValue(pupil1)
        databasePupils.child("${pupil2.id}").setValue(pupil2)
        databasePupils.child("${pupil3.id}").setValue(pupil3)
        val dbhelper = TestBase()


        ///////////////Initializing DataBase
        //db.execSQL("DROP TABLE Pupils")
        //db.execSQL("CREATE TABLE IF NOT EXISTS Pupils (id INTEGER, Avatar TEXT, Groupname TEXT, Name TEXT, Surname TEXT, Thirdname TEXT, Hashcode INTEGER)")
        //db.execSQL("INSERT INTO Pupils VALUES (0, 'https://static.mk.ru/upload/entities/2020/04/14/15/articles/detailPicture/f9/aa/3a/55/eb9f0dcbfe069ff7c772b31e88c4210b.jpg', '27тп', 'Андрей', 'Паска', 'Сергеевич', null);")
        //db.execSQL("INSERT INTO Pupils VALUES (1, null, '27тп', 'Владислав', 'Петров', 'ХЗ', null);")
        //db.execSQL("INSERT INTO Pupils VALUES (2, null, '27тп', 'Антон', 'Юлбарисов', 'ХЗ', null);")
        ///////////////
        //db.execSQL("UPDATE Pupil SET Hashcode = null WHERE id == 0;")
        StartLoading()
        DeviceLogin()
        dbhelper.ex_main()
        if (checkLocationPermission()) {
            val service = Intent(this, testPosition::class.java)
            Log.d("ASD", "startService()")

            //startService(service)
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
        var hashlist : ArrayList<Int?> = ArrayList()
        //databasePupils.addValueEventListener(object : ValueEventListener {
        //    override fun onCancelled(p0: DatabaseError) {
        //    }

        //    override fun onDataChange(dataSnapshot: DataSnapshot) {
        //        hashlist.clear()
        //            dataSnapshot.children.forEach { it.child("hashcode").value?.let { hashlist.add(it.toString().toInt()) } }
        //    }
        //})
        var query: Cursor = db.rawQuery("SELECT Hashcode FROM Pupils;", null)
        if (query.moveToFirst()) {
            do {
                hashlist.add(query.getInt(query.getColumnIndex("Hashcode")))
            } while (query.moveToNext())
        }

        if (!hashlist.contains(GenerateId().hashCode())) {
            query.close()
            db.close()
            EndLoading()
            mainPresenter.login(isSuccess = false)
        }
        else  {
            query = db.rawQuery("SELECT Name, Surname FROM Pupils WHERE Hashcode == "+GenerateId().hashCode()+";", null)
            query.moveToNext()
            mtxt_hello_user.text = "Здравствуй ${query.getString(query.getColumnIndex("Surname"))} ${query.getString(query.getColumnIndex("Name"))}!"
            mainPresenter.login(isSuccess = true)
            query.close()
            db.close()
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
