package com.example.attendancechecker.providers

import android.os.Handler
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.PupilPresenter

class PupilProvider(var presenter: PupilPresenter) {
    var pupilsList: ArrayList<PupilModel> = ArrayList()
    fun TestLoadPupils() {
            Handler().postDelayed({
                presenter.PupilsLoaded(pupilList = pupilsList)
            }, 2000)
    }
}