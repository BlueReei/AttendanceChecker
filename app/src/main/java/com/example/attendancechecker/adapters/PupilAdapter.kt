package com.example.attendancechecker.adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    fun GenerateId() : String {
        val uniquePseudoID = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        return uniquePseudoID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pupil_cell, parent, false)
        val pupilViewHolder = PupilViewHolder(itemView = itemView)
        pupilViewHolder.itemView.setOnClickListener {
            val position = pupilViewHolder.adapterPosition
            val db by lazy { parent.context.openOrCreateDatabase("Colledge_BD.db", Context.MODE_PRIVATE, null) }
            if (position != RecyclerView.NO_POSITION) {
                db.rawQuery("UPDATE Pupils SET Hashcode = "+GenerateId().hashCode()+" WHERE id == "+pupilViewHolder.adapterPosition+";", null)
                var query = db.rawQuery("SELECT id, 'Group', Name, Surname, Thirdname, Hashcode FROM Pupils WHERE id == "+pupilViewHolder.adapterPosition+";", null)
                query.moveToNext()
                    Toast.makeText(parent.context, "" +
                            "${query.getInt(query.getColumnIndex("id"))} " +
                            "${query.getString(query.getColumnIndex("Group"))} " +
                            "${query.getString(query.getColumnIndex("Surname"))} " +
                            "${query.getString(query.getColumnIndex("Name"))} " +
                            "${query.getString(query.getColumnIndex("Thirdname"))} " +
                            "${query.getInt(query.getColumnIndex("Hashcode"))}", Toast.LENGTH_SHORT).show()
                db.close()
            }
        }
        return pupilViewHolder
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