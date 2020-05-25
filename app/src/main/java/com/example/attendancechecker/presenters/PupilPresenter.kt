package com.example.attendancechecker.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.providers.PupilProvider
import com.example.attendancechecker.views.PupilView

@InjectViewState
class PupilPresenter : MvpPresenter<PupilView>() {
    fun LoadPupils() {
        viewState.StartLoading()
        PupilProvider(presenter = this).TestLoadPupils()
    }

    fun PupilsLoaded(pupilList: ArrayList<PupilModel>) {
        viewState.EndLoading()
        viewState.SetupPupilList(pupilList = pupilList)
    }

}