package com.alcorp.myactivity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.myactivity.R
import com.alcorp.myactivity.database.repository.ActivityEntity
import com.alcorp.myactivity.tools.dateToString
import com.alcorp.myactivity.tools.getCurrentDate
import com.alcorp.myactivity.ui.activity.DetailCompletedActivity
import com.alcorp.myactivity.ui.activity.AddEditActivity
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_DATE
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_DESC
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_ID
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_ISDONE
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_TIME_END
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_TIME_START
import com.alcorp.myactivity.ui.activity.AddEditActivity.Companion.EXTRA_TITLE

class ActivityAdapter(private val list: List<ActivityEntity>, private val context: Context) :
    RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitleFinalValue)
        val date: TextView = itemView.findViewById(R.id.dateFinalValue)
        val startTime: TextView = itemView.findViewById(R.id.tvRangeTimeStartValueFinal)
        val endTime: TextView = itemView.findViewById(R.id.tvRangeTimeEndValueFinal)
        val desc: TextView = itemView.findViewById(R.id.tvDescValueFinal)
        val notificationAlert: ImageView = itemView.findViewById(R.id.notificationAlertRv)
        val lateIcon: ImageView = itemView.findViewById(R.id.lateIcon)
        val doneIcon: ImageView = itemView.findViewById(R.id.checklistIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = list[position]

        holder.title.text = activity.title
        holder.startTime.text = activity.timeStart
        holder.endTime.text = activity.timeEnd
        holder.desc.text = activity.desc
        holder.date.text = dateToString(activity.date)

        // Visibility of icons based on activity properties
        holder.doneIcon.visibility =
            if ((activity.date == getCurrentDate() && activity.isDone) || activity.isDone) View.VISIBLE else View.GONE
        holder.notificationAlert.visibility =
            if ((activity.date == getCurrentDate() && !activity.isDone)) View.VISIBLE else View.GONE
        holder.lateIcon.visibility =
            if (activity.date!! < getCurrentDate() && !activity.isDone) View.VISIBLE else View.GONE

        // Set click listener for item view
        val targetActivity =
            if (activity.isDone) DetailCompletedActivity::class.java else AddEditActivity::class.java
        holder.itemView.setOnClickListener {
            val intent = Intent(context, targetActivity)
            intent.putExtra(EXTRA_ID, activity.id)
            intent.putExtra(EXTRA_TITLE, activity.title)
            intent.putExtra(EXTRA_DATE, activity.date)
            intent.putExtra(EXTRA_TIME_START, activity.timeStart)
            intent.putExtra(EXTRA_TIME_END, activity.timeEnd)
            intent.putExtra(EXTRA_DESC, activity.desc)
            intent.putExtra(EXTRA_ISDONE, activity.isDone)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}