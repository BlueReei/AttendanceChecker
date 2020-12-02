package com.example.attendancechecker.adapters

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancechecker.R
import com.example.attendancechecker.Services.DB
import com.example.attendancechecker.activities.PupilListActivity
import com.example.attendancechecker.models.PupilModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.sql.Connection
import java.sql.Statement


class PupilAdapter(context: Context, activity: PupilListActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context = context
    var activity = activity
    private var mSourceList: ArrayList<PupilModel> = ArrayList()
    private var pupilsList : ArrayList<PupilModel> = ArrayList()

    class PupilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mPupilAvatar : CircleImageView = itemView.findViewById(R.id.pupil_avatar)
        var mTxtPupilGroup : TextView = itemView.findViewById(R.id.txt_pupil_group)
        var mTxtPupilUserName : TextView = itemView.findViewById(R.id.txt_pupil_username)

        fun bind(pupilModel: PupilModel) {
            pupilModel.Avatar?.let { url -> Picasso.get().load(url).into(mPupilAvatar) }
            mTxtPupilGroup.text = "${pupilModel.Groupname}"
            mTxtPupilUserName.text = "${pupilModel.Surname} ${pupilModel.Name} ${pupilModel.Thirdname}"

        }
    }

    fun GenerateId() : String {
        val uniquePseudoID = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10
        return uniquePseudoID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.pupil_cell,
            parent,
            false
        )
        val pupilViewHolder = PupilViewHolder(itemView = itemView)
        val db = DB()
        val connection: Connection = db.getConnection()!!
        connection.autoCommit = false
        val st: Statement = connection.createStatement()
        pupilViewHolder.itemView.setOnClickListener {
            val position = pupilViewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                st.executeUpdate("UPDATE pupils SET hashcode = ${GenerateId().hashCode()} WHERE id = ${pupilViewHolder.adapterPosition};")
                st.close()
                connection.commit()
                connection.close()
                var confirmDialog = ConfirmDialog(2)
                confirmDialog.show(activity.supportFragmentManager, "")
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
        var confirmDialog = ConfirmDialog(1)
        confirmDialog.show(activity.supportFragmentManager, "")
    }

    /*private fun restartApp() {
        val intent = Intent(
            context.applicationContext,
            MainActivity::class.java
        )
        val mPendingIntentId = 125
        val mPendingIntent = PendingIntent.getActivity(
            context.applicationContext,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
        exitProcess(0)
    }*/

    fun filter(query: String) {
        pupilsList.clear()
        mSourceList.forEach {
            if (it.Name.contains(query, ignoreCase = true) || it.Surname.contains(
                    query,
                    ignoreCase = true
                ) || it.Thirdname.contains(query, ignoreCase = true) || it.Groupname.contains(
                    query,
                    ignoreCase = true
                )) {
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

class ConfirmDialog(id: Int) : AppCompatDialogFragment() {
var idd = id
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (idd == 1) {
            return activity?.let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Выберете себя из предложенного списка")
                    .setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
        else {
            return activity?.let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)
                builder.setMessage("Вы подтвердили свою личность")
                    .setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, id ->
                            it.finish()
                        })
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}