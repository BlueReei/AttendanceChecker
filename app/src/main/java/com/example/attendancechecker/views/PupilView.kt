package com.example.attendancechecker.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.attendancechecker.models.PupilModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface PupilView : MvpView {
    fun ShowError(text:String)
    fun SetupPupilList(pupilList: ArrayList<PupilModel>)
    fun StartLoading()
    fun EndLoading()
}