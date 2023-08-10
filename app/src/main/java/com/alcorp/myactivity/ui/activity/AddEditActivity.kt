package com.alcorp.myactivity.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.myactivity.MainActivity
import com.alcorp.myactivity.R
import com.alcorp.myactivity.data.ActivityViewModel
import com.alcorp.myactivity.databinding.ActivityAddEditBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

@AndroidEntryPoint
class AddEditActivity : AppCompatActivity() {

    private var activityId by Delegates.notNull<Int>()
    private val calendar = Calendar.getInstance()

    private lateinit var binding: ActivityAddEditBinding
    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActivityDetails()
        setupActionListeners()
    }

    private fun setupActivityDetails() {
        activityId = intent.getIntExtra("id", 0)
        if (activityId != 0) {
            binding.addEditTitle.text = getString(R.string.activity_edit_title)
            binding.switchDesc.visibility = View.VISIBLE
            binding.switchDoneOrNot.visibility = View.VISIBLE
            binding.btnDeleteActivity.visibility = View.VISIBLE

            binding.activityNameField.setText(intent.getStringExtra("title"))
            binding.dateActivityField.setText(intent.getStringExtra("date"))
            binding.descriptionActivityField.setText(intent.getStringExtra("desc"))
            binding.startTimeField.setText(intent.getStringExtra("timeStart"))
            binding.endTimeField.setText(intent.getStringExtra("timeEnd"))
        } else {
            binding.switchDesc.visibility = View.GONE
            binding.switchDoneOrNot.visibility = View.GONE
            binding.btnDeleteActivity.visibility = View.GONE
            binding.addEditTitle.text = getString(R.string.activity_add_title)
        }
        setClickListeners()
    }

    private fun setupActionListeners() {
        binding.btnSubmit.setOnClickListener {
            validateAndSaveActivity()
        }
        binding.btnDeleteActivity.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setClickListeners() {
        binding.dateActivityField.isFocusableInTouchMode = false
        binding.startTimeField.isFocusableInTouchMode = false
        binding.endTimeField.isFocusableInTouchMode = false

        binding.dateActivityField.setOnClickListener {
            showDatePickerDialog()
        }
        binding.startTimeField.setOnClickListener {
            showTimePickerDialog(binding.startTimeField)
        }
        binding.endTimeField.setOnClickListener {
            showTimePickerDialog(binding.endTimeField)
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmation_activity_title))
            .setMessage(getString(R.string.delete_activity_message))
            .setPositiveButton(getString(R.string.delete_activity_positive_button)) { dialog, _ ->
                dialog.dismiss()
                deleteActivity()
            }
            .setNegativeButton(getString(R.string.delete_activity_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteActivity() {
        activityViewModel.deleteById(activityId)
        Toast.makeText(
            this@AddEditActivity,
            getString(R.string.delete_activity_success_message),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@AddEditActivity, MainActivity::class.java))
        finishAffinity()
    }

    private fun showDatePickerDialog() {
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateField()
            }

        val datePickerDialog = DatePickerDialog(
            this,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun updateDateField() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.dateActivityField.setText(dateFormat.format(calendar.time))
    }

    private var selectedStartTime: Long = 0
    private var selectedEndTime: Long = 0
    private fun showTimePickerDialog(textInputEditText: TextInputEditText) {
        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                if (textInputEditText.id == R.id.startTimeField) {
                    selectedStartTime = calendar.timeInMillis
                    updateTimeField(textInputEditText)
                    binding.endTimeField.setText("")
                } else {
                    val startTime = selectedStartTime
                    val endTime = calendar.timeInMillis

                    if (endTime <= startTime) {
                        textInputEditText.error = getString(R.string.required_end_time_greater)
                        Toast.makeText(
                            this@AddEditActivity,
                            getString(R.string.required_end_time_greater),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        selectedEndTime = endTime
                        updateTimeField(textInputEditText)
                        textInputEditText.error = null
                    }
                }
            }

        val timePickerDialog = TimePickerDialog(
            this,
            timeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()
    }

    private fun updateTimeField(textInputEditText: TextInputEditText) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        textInputEditText.setText(timeFormat.format(calendar.time))
    }

    private fun validateAndSaveActivity() {
        val title = binding.activityNameField.text.toString().trim()
        val date = binding.dateActivityField.text.toString().trim()
        val desc = binding.descriptionActivityField.text.toString().trim()
        val startTime = binding.startTimeField.text.toString().trim()
        val endTime = binding.endTimeField.text.toString().trim()

        when {
            title.isBlank() -> {
                showErrorAndToast(binding.activityNameField, R.string.required_title)
            }

            date.isBlank() -> {
                showErrorAndToast(binding.dateActivityField, R.string.required_date)
            }

            startTime.isBlank() -> {
                showErrorAndToast(binding.startTimeField, R.string.required_start_time)
            }

            endTime.isBlank() -> {
                showErrorAndToast(binding.endTimeField, R.string.required_end_time)
            }

            desc.isBlank() -> {
                showErrorAndToast(binding.descriptionActivityField, R.string.required_description)
            }

            else -> {
                saveActivity(
                    title,
                    date,
                    desc,
                    startTime,
                    endTime,
                    binding.switchDoneOrNot.isChecked
                )
            }
        }
    }

    private fun showErrorAndToast(textInputEditText: TextInputEditText, errorMessageResId: Int) {
        textInputEditText.error = getString(errorMessageResId)
        Toast.makeText(this, getString(errorMessageResId), Toast.LENGTH_SHORT).show()
    }

    private fun saveActivity(
        title: String,
        date: String,
        desc: String,
        startTime: String,
        endTime: String,
        isDone: Boolean
    ) {
        val activity = ActivityEntity(
            id = activityId,
            date = date,
            timeStart = startTime,
            timeEnd = endTime,
            title = title,
            desc = desc,
            isDone = isDone
        )

        if (activityId != 0 && !isDone) {
            activityViewModel.update(activity)
            showToast(R.string.activity_success_edit_message)
        } else if (activityId != 0 && isDone) {
            activityViewModel.update(activity)
            showToast(R.string.activity_success_done_message)
        } else {
            activityViewModel.insert(activity)
            showToast(R.string.activity_success_add_message)
        }
        navigateToMainActivity()
    }

    private fun showToast(messageResId: Int) {
        runOnUiThread {
            Toast.makeText(this@AddEditActivity, getString(messageResId), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToMainActivity() {
        runOnUiThread {
            startActivity(Intent(this@AddEditActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

}