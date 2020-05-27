package com.example.attendancechecker.providers

import android.content.Context
import android.os.Handler
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.PupilPresenter

class PupilProvider(var presenter: PupilPresenter) {
    var pupilsList: ArrayList<PupilModel> = ArrayList()

    fun TestLoadPupils(context: Context) {
            Handler().postDelayed({
                val db by lazy { context.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }
                var query = db.rawQuery("SELECT * FROM Pupils WHERE id == 0;", null)
                query.moveToNext()
                val pupil0 = PupilModel(
                    id = query.getInt(query.getColumnIndex("id")),
                    Avatar = query.getString(query.getColumnIndex("Avatar")),
                    Group = query.getString(query.getColumnIndex("Group")),
                    Name = query.getString(query.getColumnIndex("Name")),
                    Surname = query.getString(query.getColumnIndex("Surname")),
                    Thirdname = query.getString(query.getColumnIndex("Thirdname")),
                    Hashcode = query.getInt(query.getColumnIndex("Hashcode")))
                query = db.rawQuery("SELECT * FROM Pupils WHERE id == 1;", null)
                query.moveToNext()
                val pupil1 = PupilModel(
                    id = query.getInt(query.getColumnIndex("id")),
                    Avatar = query.getString(query.getColumnIndex("Avatar")),
                    Group = query.getString(query.getColumnIndex("Group")),
                    Name = query.getString(query.getColumnIndex("Name")),
                    Surname = query.getString(query.getColumnIndex("Surname")),
                    Thirdname = query.getString(query.getColumnIndex("Thirdname")),
                    Hashcode = query.getInt(query.getColumnIndex("Hashcode")))
                query = db.rawQuery("SELECT * FROM Pupils WHERE id == 2;", null)
                query.moveToNext()
                val pupil2 = PupilModel(
                    id = query.getInt(query.getColumnIndex("id")),
                    Avatar = query.getString(query.getColumnIndex("Avatar")),
                    Group = query.getString(query.getColumnIndex("Group")),
                    Name = query.getString(query.getColumnIndex("Name")),
                    Surname = query.getString(query.getColumnIndex("Surname")),
                    Thirdname = query.getString(query.getColumnIndex("Thirdname")),
                    Hashcode = query.getInt(query.getColumnIndex("Hashcode")))
                query.close()
                db.close()
                pupilsList.add(pupil0)
                pupilsList.add(pupil1)
                pupilsList.add(pupil2)
                presenter.PupilsLoaded(pupilList = pupilsList)
            }, 2000)
    }
}
