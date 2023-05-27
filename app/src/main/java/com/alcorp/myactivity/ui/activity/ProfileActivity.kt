package com.alcorp.myactivity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.alcorp.myactivity.database.preferences.SettingsPreferences
import com.alcorp.myactivity.database.repository.ProfileRepository
import com.alcorp.myactivity.database.viewmodel.MainViewModel
import com.alcorp.myactivity.database.viewmodel.ViewModelFactory
import com.alcorp.myactivity.databinding.ActivityProfileBinding
import com.alcorp.myactivity.tools.intToBitmap

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileRepository: ProfileRepository
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModels()
        setupRepositories()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModels() {
        val pref = SettingsPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[MainViewModel::class.java]
    }

    private fun setupRepositories() {
        profileRepository = ProfileRepository(application)
    }

    private fun setupObservers() {
        profileRepository.getProfile().observe(this) { profile ->
            profile?.let {
                binding.nameProfile.text = it.name
                binding.profileImage.setImageBitmap(it.photo?.let { photo ->
                    intToBitmap(
                        this,
                        photo
                    )
                })
            }
        }

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val nightMode =
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
            binding.nightModeSwitch.isChecked = isDarkModeActive
        }
    }

    private fun setupListeners() {
        binding.editProfileBtn.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        binding.settingBackBtn.setOnClickListener {
            finish()
        }

        binding.changeLanguageBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}