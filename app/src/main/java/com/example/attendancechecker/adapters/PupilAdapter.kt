package com.example.attendancechecker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancechecker.models.PupilModel
import com.example.attendancechecker.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PupilAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mSourceList: ArrayList<PupilModel> = ArrayList()
    private var pupilsList : ArrayList<PupilModel> = ArrayList()

    class PupilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPupilAvatar : CircleImageView = itemView.findViewById(R.id.pupil_avatar)
        var mTxtPupilGroup : TextView = itemView.findViewById(R.id.txt_pupil_group)
        var mTxtPupilUserName : TextView = itemView.findViewById(R.id.txt_pupil_username)

        fun bind(pupilModel: PupilModel) {
            pupilModel.Avatar?.let {url -> Picasso.get().load(url).into(mPupilAvatar) }
            mTxtPupilGroup.text = "${pupilModel.Group}"
            mTxtPupilUserName.text = "${pupilModel.Surname} ${pupilModel.Name} ${pupilModel.Thirdname}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pupil_cell, parent, false)
        val pupilViewHolder = PupilViewHolder(itemView = itemView)
        pupilViewHolder.itemView.rootView.setOnClickListener {
            val position = pupilViewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                    Toast.makeText(parent.context, pupilViewHolder.adapterPosition, Toast.LENGTH_SHORT).show()
            }
        }
        return PupilViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return pupilsList.count()
    }
    fun SetupPupils(pupilList: ArrayList<PupilModel>) {
        mSourceList.clear()
        mSourceList.addAll(pupilList)
        filter(query = "")
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        pupilsList.clear()
        mSourceList.forEach {
            if (it.Name.contains(query, ignoreCase = true) || it.Surname.contains(query, ignoreCase = true) || it.Thirdname.contains(query, ignoreCase = true) || it.Group.contains(query, ignoreCase = true)) {
                pupilsList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PupilViewHolder) {
            holder.bind(pupilModel = pupilsList[holder.adapterPosition])
        }
    }
}