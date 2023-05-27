package com.alcorp.myactivity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alcorp.myactivity.R
import com.alcorp.myactivity.database.repository.ActivityRepository
import com.alcorp.myactivity.databinding.ActivityDetailCompletedBinding
import com.alcorp.myactivity.tools.dateToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class DetailCompletedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCompletedBinding
    private lateinit var activityRepository: ActivityRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActivityRepository()
        setupActivityDetails()
        setupActionListeners()
    }

    private fun setupActivityRepository() {
        activityRepository = ActivityRepository(application)
    }

    private fun setupActivityDetails() {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val date = dateToString(intent.getStringExtra(EXTRA_DATE))
        val timeStart = intent.getStringExtra(EXTRA_TIME_START)
        val timeEnd = intent.getStringExtra(EXTRA_TIME_END)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val isDone = intent.getBooleanExtra(EXTRA_ISDONE, false)

        binding.tvTitleIdValueFinal.text = id.toString()
        binding.tvTitleFinalValue.text = title
        binding.tvDateValueFinal.text = date
        binding.tvRangeTimeStartValueFinal.text = timeStart
        binding.tvRangeTimeEndValueFinal.text = timeEnd
        binding.tvDescValueFinal.text = desc
        binding.tvStatusValueFinal.text = if (isDone) {
            resources.getString(R.string.activity_status_completed)
        } else {
            resources.getString(R.string.activity_status_uncompleted)
        }
    }

    private fun setupActionListeners() {
        binding.btnCloseDoneActivity.setOnClickListener {
            finish()
        }
        binding.btnDeleteActivity.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.confirmation_activity_title)
        builder.setMessage(R.string.delete_activity_message)

        builder.setPositiveButton(R.string.delete_activity_positive_button) { dialog, _ ->
            dialog.dismiss()
            deleteActivityAndNavigateToMainScreen()
        }

        builder.setNegativeButton(R.string.delete_activity_negative_button) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteActivityAndNavigateToMainScreen() {
        val activityId = binding.tvTitleIdValueFinal.text.toString().toInt()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                activityRepository.deleteActivityById(activityId)
            }
        }

        Toast.makeText(
            this,
            R.string.delete_activity_success_message,
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_TIME_START = "extra_time_start"
        const val EXTRA_TIME_END = "extra_time_end"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_ISDONE = "extra_isdone"
    }
}