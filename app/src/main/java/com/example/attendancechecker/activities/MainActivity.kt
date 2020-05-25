package com.example.attendancechecker.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.attendancechecker.R
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.MainPresenter
import com.example.attendancechecker.views.MainView
import com.github.rahatarmanahmed.cpv.CircularProgressView


class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var mcpv_circural_bar: CircularProgressView
    private lateinit var mtxt_hello_user: TextView
    val pupilsList: ArrayList<PupilModel> = ArrayList()


    @InjectPresenter
    lateinit var mainPresenter : MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mtxt_hello_user = findViewById(R.id.txt_hello_user)
        mcpv_circural_bar = findViewById(R.id.cpv_circural_bar)
        var pupil1 = PupilModel(Avatar = "https://static.mk.ru/upload/entities/2020/04/14/15/articles/detailPicture/f9/aa/3a/55/eb9f0dcbfe069ff7c772b31e88c4210b.jpg", Group = "27тп", Name = "Андрей", Surname = "Паска", Thirdname = "Сергеевич", Hashcode = null)
        var pupil2 = PupilModel(Avatar = null, Group = "27тп", Name = "Владислав", Surname = "Петров", Thirdname = "ХЗ", Hashcode = null)
        var pupil3 = PupilModel(Avatar = null, Group = "27тп", Name = "Антон", Surname = "Юлбарисов", Thirdname = "ХЗ", Hashcode = null)
        pupilsList.add(pupil1)
        pupilsList.add(pupil2)
        pupilsList.add(pupil3)
        StartLoading()
        DeviceLogin()
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
        var HashList : ArrayList<Int?> = ArrayList()
        pupilsList.forEach { HashList.plus(it) }
        if (HashList.contains(GenerateId().hashCode())) {
            mainPresenter.login(isSuccess = true)
        } else {
            mainPresenter.login(isSuccess = false)
        }
    }

    /* override fun DeviceCheck() {
         var hashlist = listOf<Int>()
         var query: Cursor = db.rawQuery("SELECT Hashcode FROM Pupils;", null)
         if (query.moveToFirst()) {
             do {
                 hashlist += query.getInt(4)
             } while (query.moveToNext())
         }

         if (!hashlist.contains(GenerateId().hashCode())) {
             query.close()
             db.close()
             EndLoading()
             startActivity(intent)
         }
         else  {
             query = db.rawQuery("SELECT Name, Surname FROM Pupils WHERE Hashcode == "+GenerateId().hashCode()+";", null)
             query.moveToFirst()
             mtxt_hello_user.text = "Здравствуй ${query.getString(1)} ${query.getString(0)}!"
             EndLoading()
             query.close()
             db.close()
         }

     }*/

    override fun ShowError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun OpenPupilList() {
        startActivity(Intent(applicationContext, PupilListActivity::class.java))
    }
}
