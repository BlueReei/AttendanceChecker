package com.example.attendancechecker.providers

import android.content.Context
import android.os.Handler
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.PupilPresenter
import com.google.firebase.database.*


class PupilProvider(var presenter: PupilPresenter) {
    var pupilsList: ArrayList<PupilModel> = ArrayList()

    fun TestLoadPupils(context: Context) {
            Handler().postDelayed({
                val databasePupils : DatabaseReference = FirebaseDatabase.getInstance().getReference("Pupils")
                databasePupils.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pupilsList.clear()
                        for (postSnapshot in dataSnapshot.children) {
                            //getting pupil
                            val pupil: PupilModel? = postSnapshot.getValue(PupilModel::class.java)
                            //adding pupil to the list
                            pupilsList.add(pupil!!)
                        }
                    }
                })
                if (pupilsList.count() != 0) presenter.PupilsLoaded(pupilList = pupilsList) else TestLoadPupils(context)
            }, 2000)
    }
}
