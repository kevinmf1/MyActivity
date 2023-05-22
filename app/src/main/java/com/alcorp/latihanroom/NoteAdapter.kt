package com.alcorp.latihanroom

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_DESC
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_ID
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_ISDONE
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_TIME_END
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_TIME_START
import com.alcorp.latihanroom.TambahUbahNoteActivity.Companion.EXTRA_TITLE

class NoteAdapter(private val list: List<NoteEntity>, private val context: Context) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitleFinalValue)
        val dateStart: TextView = itemView.findViewById(R.id.tvRangeTimeStartValueFinal)
        val dateEnd: TextView = itemView.findViewById(R.id.tvRangeTimeEndValueFinal)
        val desc: TextView = itemView.findViewById(R.id.tvDescValueFinal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.desc.text = list[position].desc
        holder.dateStart.text = list[position].timeStart
        holder.dateEnd.text = list[position].timeEnd

        if (list[position].isDone) {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailArchiveActivity::class.java)
                intent.putExtra(EXTRA_ID, list[position].id)
                intent.putExtra(EXTRA_TITLE, list[position].title)
                intent.putExtra(EXTRA_TIME_START, list[position].timeStart)
                intent.putExtra(EXTRA_TIME_END, list[position].timeEnd)
                intent.putExtra(EXTRA_DESC, list[position].desc)
                intent.putExtra(EXTRA_ISDONE, list[position].isDone)
                context.startActivity(intent)
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, TambahUbahNoteActivity::class.java)
                intent.putExtra(EXTRA_ID, list[position].id)
                intent.putExtra(EXTRA_TITLE, list[position].title)
                intent.putExtra(EXTRA_TIME_START, list[position].timeStart)
                intent.putExtra(EXTRA_TIME_END, list[position].timeEnd)
                intent.putExtra(EXTRA_DESC, list[position].desc)
                intent.putExtra(EXTRA_ISDONE, true)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}