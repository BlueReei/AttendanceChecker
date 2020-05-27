package com.example.attendancechecker.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.attendancechecker.R
import com.example.attendancechecker.adapters.PupilAdapter
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.presenters.PupilPresenter
import com.example.attendancechecker.views.PupilView
import com.github.rahatarmanahmed.cpv.CircularProgressView
import tel.egram.kuery.sqlite.text
import java.text.FieldPosition

class PupilListActivity : MvpAppCompatActivity(), PupilView {

    private lateinit var mAdapter: PupilAdapter
    private lateinit var mCpvCirclePupils: CircularProgressView
    private lateinit var mRecyclerPupils: RecyclerView

    @InjectPresenter
    lateinit var pupilPresenter: PupilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pupil_list)
        mCpvCirclePupils = findViewById(R.id.cpv_pupils)
        mRecyclerPupils = findViewById(R.id.recycler_pupils)
        pupilPresenter.LoadPupils(baseContext)
        mAdapter = PupilAdapter()
        var mTxtEditPupil = findViewById<EditText>(R.id.txt_pupil_search)
        mTxtEditPupil.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mAdapter.filter(query = p0.toString())
            }

        })
        mRecyclerPupils.adapter = mAdapter
        mRecyclerPupils.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        mRecyclerPupils.setHasFixedSize(true)
    }

    override fun ShowError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun SetupPupilList(PupilList: ArrayList<PupilModel>) {
        mAdapter.SetupPupils(pupilList = PupilList)
    }

    override fun StartLoading() {
        mCpvCirclePupils.visibility = View.VISIBLE
    }

    override fun EndLoading() {
        mCpvCirclePupils.visibility = View.GONE
    }
}
