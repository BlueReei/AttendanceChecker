package com.example.attendancechecker.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.attendancechecker.R
import com.example.attendancechecker.Services.PositionService
import com.example.attendancechecker.presenters.MainPresenter
import com.example.attendancechecker.views.MainView
import com.github.rahatarmanahmed.cpv.CircularProgressView


class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mcpv_circural_bar: CircularProgressView
    private lateinit var mtxt_hello_user: TextView
    val db by lazy { baseContext.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }


    @InjectPresenter
    lateinit var mainPresenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mtxt_hello_user = findViewById(R.id.txt_hello_user)
        mcpv_circural_bar = findViewById(R.id.cpv_circural_bar)


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

        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            val service = Intent(this, PositionService::class.java)
            startService(service)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123)
            var grantResults = IntArray(0)
            onRequestPermissionsResult(123,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),grantResults = grantResults)
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == 123 && grantResults.size == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val service = Intent(this, PositionService::class.java)
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
}
