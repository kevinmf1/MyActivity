package com.alcorp.latihanroom

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alcorp.latihanroom.databinding.ActivityDetailArchiveBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailArchiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArchiveBinding
    private lateinit var mNoteRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.colorDetailArchive
                )
            )
        )
        actionBar!!.title = "Detail Arsip Aktivitas"
        actionBar.setDisplayHomeAsUpEnabled(true)

        mNoteRepository = NoteRepository(application)

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val timeStart = intent.getStringExtra(EXTRA_TIME_START)
        val timeEnd = intent.getStringExtra(EXTRA_TIME_END)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val isDone = intent.getBooleanExtra(EXTRA_ISDONE, false)

        binding.tvTitleIdValueFinal.text = id.toString()
        binding.tvTitleFinalValue.text = title
        binding.tvRangeTimeStartValueFinal.text = timeStart
        binding.tvRangeTimeEndValueFinal.text = timeEnd
        binding.tvDescValueFinal.text = desc
        binding.tvStatusValueFinal.text =
            if (isDone) "Aktivitas sudah selesai" else "Aktivitas belum selesai"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_archive -> {

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        mNoteRepository.deleteNoteById(
                            binding.tvTitleIdValueFinal.text.toString().toInt()
                        )
                    }

                }
                Toast.makeText(this, "Arsip aktivitas berhasil dihapus", Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TIME_START = "extra_time_start"
        const val EXTRA_TIME_END = "extra_time_end"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_ISDONE = "extra_isdone"
        // private const val REQUEST_DONE_TASK_ACTIVITY = 1
    }
}