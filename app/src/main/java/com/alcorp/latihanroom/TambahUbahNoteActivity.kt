package com.alcorp.latihanroom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.alcorp.latihanroom.databinding.ActivityTambahUbahNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.properties.Delegates


class TambahUbahNoteActivity : AppCompatActivity() {

    private lateinit var mNoteRepository: NoteRepository
    private var getId by Delegates.notNull<Int>()

    private lateinit var binding: ActivityTambahUbahNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahUbahNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mNoteRepository = NoteRepository(application)
        getId = intent.getIntExtra(EXTRA_ID, 0)
        if (getId != 0) {
            val actionBar = supportActionBar
            actionBar?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.ColorEdit
                    )
                )
            )
            actionBar!!.title = "Edit Aktivitas"

            binding.edtTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.edtDesc.setText(intent.getStringExtra(EXTRA_DESC))
            binding.editStartTime.setText(intent.getStringExtra(EXTRA_TIME_START))
            binding.editEndTime.setText(intent.getStringExtra(EXTRA_TIME_END))
        } else {
            val actionBar = supportActionBar
            actionBar?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.ColorAdd
                    )
                )
            )
            actionBar!!.title = "Tambah Aktivitas"
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val desc = binding.edtDesc.text.toString()
            val startTime = binding.editStartTime.text.toString()
            val endTime = binding.editEndTime.text.toString()

            if (title == "") {
                Toast.makeText(this, "Title tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (desc == "") {
                Toast.makeText(this, "Desc tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (startTime == "") {
                Toast.makeText(this, "Start Time tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (endTime == "") {
                Toast.makeText(this, "End Time tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (getId != 0) {
                    // Update
                    val note = NoteEntity(
                        id = getId,
                        timeStart = binding.editStartTime.text.toString(),
                        timeEnd = binding.editEndTime.text.toString(),
                        title = title,
                        desc = desc
                    )

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            mNoteRepository.update(note)
                        }
                        Toast.makeText(
                            this@TambahUbahNoteActivity,
                            "Data berhasil diubah",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@TambahUbahNoteActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                } else {
                    // Insert
                    val note = NoteEntity(
                        timeStart = binding.editStartTime.text.toString(),
                        timeEnd = binding.editEndTime.text.toString(),
                        title = title,
                        desc = desc
                    )

                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            mNoteRepository.insert(note)
                        }
                        Toast.makeText(
                            this@TambahUbahNoteActivity,
                            "Data berhasil dimasukan",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@TambahUbahNoteActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        if (getId != 0) inflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> {
                // Update
                val note = NoteEntity(
                    id = getId,
                    timeStart = binding.editStartTime.text.toString(),
                    timeEnd = binding.editEndTime.text.toString(),
                    title = binding.edtTitle.text.toString(),
                    desc = binding.edtDesc.text.toString(),
                    isDone = true
                )

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        mNoteRepository.update(note)
                    }
                    Toast.makeText(
                        this@TambahUbahNoteActivity,
                        "Aktivitas telah selesai",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@TambahUbahNoteActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }

            }
            R.id.action_delete -> {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        mNoteRepository.deleteNoteById(getId)
                    }
                    Toast.makeText(
                        this@TambahUbahNoteActivity,
                        "Aktivitas berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@TambahUbahNoteActivity, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun showTimeStart(view: View?) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker?, selectedHour: Int, selectedMinute: Int ->
                        val dateTime =
                            selectedYear.toString() + "-" + (selectedMonth + 1) + "-" + selectedDay + " " + selectedHour + ":" + selectedMinute
                        val editTextDateTime = findViewById<EditText>(R.id.editStartTime)
                        editTextDateTime.setText(dateTime)
                    }, hour, minute, true
                )
                timePickerDialog.show()
            }, year, month, day
        )
        datePickerDialog.show()
    }

    fun showTimeEnd(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val dateTime =
                        "$selectedYear-${selectedMonth + 1}-$selectedDay $selectedHour:$selectedMinute"
                    val editTextDateTime = findViewById<EditText>(R.id.editEndTime)
                    editTextDateTime.setText(dateTime)
                }, hour, minute, true)
                timePickerDialog.show()
            }, year, month, day)
        datePickerDialog.show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TIME_START = "extra_time_start"
        const val EXTRA_TIME_END = "extra_time_end"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_ISDONE = "extra_isdone"
    }

}