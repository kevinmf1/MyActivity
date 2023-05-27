package com.alcorp.myactivity.ui.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alcorp.myactivity.R
import com.alcorp.myactivity.database.repository.ProfileEntity
import com.alcorp.myactivity.database.repository.ProfileRepository
import com.alcorp.myactivity.databinding.ActivityProfileEditBinding
import com.alcorp.myactivity.tools.intToBitmap
import com.alcorp.myactivity.ui.activity.ImageProfileChangeActivity.Companion.EXTRA_PROFILE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var profileRepository: ProfileRepository
    private lateinit var calendar: Calendar
    private var idProfile: Int = 0
    private var currentImage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupRepositories()
        setupObservers()
        setupListeners()
    }

    private fun initializeViews() {
        calendar = Calendar.getInstance()
    }

    private fun setupRepositories() {
        profileRepository = ProfileRepository(application)
    }

    private fun setupObservers() {
        profileRepository.getProfile().observe(this) { profile ->
            profile?.let {
                idProfile = it.id
                binding.nameInputProfileField.setText(it.name)
                binding.genderInputProfileField.setText(it.gender, false)
                binding.dateBirthInputProfileField.setText(it.dateBirth)
                binding.addressInputProfileField.setText(it.address)
                binding.phoneInputProfileField.setText(it.phone)
                currentImage = it.photo ?: 0
                binding.profileImage.setImageBitmap(it.photo?.let { it1 -> intToBitmap(this, it1) })
            }
        }
    }

    private fun setupListeners() {
        binding.settingBackBtn.setOnClickListener {
            finish()
        }

        binding.saveBtnProfile.setOnClickListener {
            showSaveConfirmationDialog()
        }

        setupPhoneInputField()

        val genderOptions = listOf(
            getString(R.string.profile_gender_value_man),
            getString(R.string.profile_gender_value_woman)
        )
        val adapter = ArrayAdapter(this, R.layout.dropdown_gender, genderOptions)
        binding.genderInputProfileField.setAdapter(adapter)

        binding.dateBirthInputProfileField.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editImageIcon.setOnClickListener {
            redirectToImageProfileActivity()
        }

        binding.profileImage.setOnClickListener {
            redirectToImageProfileActivity()
        }
    }

    private fun setupPhoneInputField() {
        binding.phoneInputProfileField.filters = arrayOf(InputFilter.LengthFilter(13))
        binding.phoneInputProfileField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                if (input.isEmpty() || input.length < 2) {
                    s?.insert(0, "08")
                }
            }
        })
    }

    private fun showSaveConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmation_activity_title))
            .setMessage(getString(R.string.profile_save_message))
            .setPositiveButton(getString(R.string.profile_btnSave)) { dialog, _ ->
                dialog.dismiss()
                saveProfile()
            }
            .setNegativeButton(getString(R.string.profile_btnCancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveProfile() {
        val profileData = ProfileEntity(
            id = idProfile,
            name = binding.nameInputProfileField.text.toString(),
            gender = binding.genderInputProfileField.text.toString(),
            dateBirth = binding.dateBirthInputProfileField.text.toString(),
            address = binding.addressInputProfileField.text.toString(),
            phone = binding.phoneInputProfileField.text.toString(),
            photo = currentImage
        )

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                profileRepository.update(profileData)
                showToast(R.string.profile_success_edit_message)
            }
            navigateToMainActivity()
        }
    }

    private fun showToast(messageResId: Int) {
        runOnUiThread {
            Toast.makeText(this@ProfileEditActivity, getString(messageResId), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToMainActivity() {
        runOnUiThread {
            startActivity(Intent(this@ProfileEditActivity, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val dateString = "$dayOfMonth-${month + 1}-$year"
                binding.dateBirthInputProfileField.setText(dateString)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun redirectToImageProfileActivity() {
        val intent = Intent(this, ImageProfileChangeActivity::class.java)
        intent.putExtra(EXTRA_PROFILE_ID, idProfile.toString())
        startActivity(intent)
    }
}