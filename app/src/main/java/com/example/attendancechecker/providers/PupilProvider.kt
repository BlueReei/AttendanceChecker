package com.example.attendancechecker.providers

import android.os.Handler
import com.example.attendancechecker.Services.DB
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.PupilPresenter
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement


class PupilProvider(var presenter: PupilPresenter) {
    var pupilsList: ArrayList<PupilModel> = ArrayList()
    val db = DB()
    private val connection: Connection = db.getConnection()!!

    fun TestLoadPupils() {
            Handler().postDelayed({
                pupilsList.clear()
                val st: Statement = connection.createStatement()
                var rs: ResultSet
                st.use { st ->
                        rs = st.executeQuery("SELECT * FROM pupils WHERE hashcode IS NULL ORDER BY id")
                        while (rs.next()) {
                            var pupil = PupilModel(rs.getInt(db.ID), rs.getString(db.AVATAR),  rs.getString(db.GROUP),  rs.getString(db.NAME),  rs.getString(db.SURNAME),  rs.getString(db.THIRDNAME), rs.getInt(db.HASHCODE),  rs.getString(db.COMEDATE), rs.getString(db.LEAVEDATE))
                            pupilsList.add(pupil)
                        }
                }
                st.close()
                connection.close()
               presenter.PupilsLoaded(pupilList = pupilsList)
            }, 2000)
    }
}
