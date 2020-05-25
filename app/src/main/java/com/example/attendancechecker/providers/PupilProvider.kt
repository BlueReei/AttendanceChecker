package com.example.attendancechecker.providers

import android.os.Handler
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.MainPresenter
import com.example.attendancechecker.presenters.PupilPresenter

class PupilProvider(var presenter: PupilPresenter) {
    var pupilsList: ArrayList<PupilModel> = ArrayList()
    fun TestLoadPupils() {
            Handler().postDelayed({
                var pupil1 = PupilModel(Avatar = "https://static.mk.ru/upload/entities/2020/04/14/15/articles/detailPicture/f9/aa/3a/55/eb9f0dcbfe069ff7c772b31e88c4210b.jpg", Group = "27тп", Name = "Андрей", Surname = "Паска", Thirdname = "Сергеевич", Hashcode = -32741541)
                var pupil2 = PupilModel(Avatar = null, Group = "27тп", Name = "Владислав", Surname = "Петров", Thirdname = "ХЗ", Hashcode = null)
                var pupil3 = PupilModel(Avatar = null, Group = "27тп", Name = "Антон", Surname = "Юлбарисов", Thirdname = "ХЗ", Hashcode = null)
                pupilsList.add(pupil1)
                pupilsList.add(pupil2)
                pupilsList.add(pupil3)
                presenter.PupilsLoaded(pupilList = pupilsList)
            }, 2000)
    }
}