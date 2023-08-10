package com.alcorp.myactivity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.core.utils.dateToString
import com.alcorp.myactivity.MainActivity
import com.alcorp.myactivity.R
import com.alcorp.myactivity.data.ActivityViewModel
import com.alcorp.myactivity.databinding.ActivityDetailCompletedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCompletedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCompletedBinding
    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActivityDetails()
        setupActionListeners()
    }

    private fun setupActivityDetails() {
        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")
        val date = dateToString(intent.getStringExtra("date"))
        val timeStart = intent.getStringExtra("timeStart")
        val timeEnd = intent.getStringExtra("timeEnd")
        val desc = intent.getStringExtra("desc")
        val isDone = intent.getBooleanExtra("isDone", false)

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

        activityViewModel.deleteById(activityId)

        Toast.makeText(
            this,
            R.string.delete_activity_success_message,
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}