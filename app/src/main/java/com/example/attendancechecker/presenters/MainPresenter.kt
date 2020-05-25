package com.example.attendancechecker.presenters

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.attendancechecker.views.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    fun login(isSuccess: Boolean) {
        Handler().postDelayed({
            if (isSuccess) {
                viewState.EndLoading()
            } else {
                viewState.OpenPupilList()
            }
        }, 500)
    }
}