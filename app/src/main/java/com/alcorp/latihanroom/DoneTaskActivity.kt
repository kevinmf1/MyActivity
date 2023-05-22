package com.alcorp.latihanroom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.latihanroom.databinding.ActivityDoneTaskBinding


class DoneTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoneTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoneTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.RED))
        actionBar!!.title = "Arsip Aktivitas"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val mNoteRepository = NoteRepository(application)
        binding.rvDoneTask.layoutManager = LinearLayoutManager(this)
        binding.rvDoneTask.addItemDecoration(
            DividerItemDecoration(
                binding.rvDoneTask.context,
                DividerItemDecoration.VERTICAL
            )
        )


        mNoteRepository.getAllDoneNotes().observe(this) { note ->
            if (note.isEmpty()) {
                binding.tvEmptyDoneTask.visibility = View.VISIBLE
            } else {
                binding.tvEmptyDoneTask.visibility = View.GONE
                val adapter = NoteAdapter(note, this@DoneTaskActivity).apply {
                    setHasStableIds(true)
                }
                binding.rvDoneTask.adapter = adapter
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}