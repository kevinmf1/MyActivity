package com.alcorp.core.ui

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.core.R
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.databinding.ItemActivityBinding
import com.alcorp.core.utils.getCurrentDate

class ActivityAdapter : ListAdapter<ActivityEntity, ActivityAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((ActivityEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemActivityBinding.bind(itemView)

        fun bind(data: ActivityEntity) {
            with(binding) {
                tvTitleFinalValue.text = data.title
                dateFinalValue.text = data.date
                tvRangeTimeStartValueFinal.text = data.timeStart
                tvRangeTimeEndValueFinal.text = data.timeEnd
                tvDescValueFinal.text = data.desc

                checklistIcon.visibility = if ((data.date == getCurrentDate() && data.isDone) || data.isDone) View.VISIBLE else View.GONE
                notificationAlertRv.visibility = if ((data.date == getCurrentDate() && !data.isDone)) View.VISIBLE else View.GONE
                lateIcon.visibility = if (data.date!! < getCurrentDate() && !data.isDone) View.VISIBLE else View.GONE

            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ActivityEntity>() {
            override fun areItemsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity) =
                oldItem == newItem
        }
    }
}