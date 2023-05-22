package com.alcorp.latihanroom

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.latihanroom.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "Daftar Aktivitas"

        val mNoteRepository = NoteRepository(application)
        binding.rvNote.layoutManager = LinearLayoutManager(this)
        binding.rvNote.addItemDecoration(
            DividerItemDecoration(
                binding.rvNote.context,
                DividerItemDecoration.VERTICAL
            )
        )

        mNoteRepository.getAllNotes().observe(this) { note ->
            if (note.isEmpty()) {
                binding.tvEmptyActivity.visibility = View.VISIBLE
            } else {
                binding.tvEmptyActivity.visibility = View.GONE
                val adapter = NoteAdapter(note, this@MainActivity).apply {
                    setHasStableIds(true)
                }
                binding.rvNote.adapter = adapter
            }
        }

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, TambahUbahNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_arsip, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        return if (id == R.id.action_settings) {
            startActivity(Intent(this, DoneTaskActivity::class.java))
            true
        } else super.onOptionsItemSelected(item)
    }


}