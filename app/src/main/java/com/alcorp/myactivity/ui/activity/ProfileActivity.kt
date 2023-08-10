package com.alcorp.myactivity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.alcorp.core.utils.intToBitmap
import com.alcorp.myactivity.data.ProfileViewModel
import com.alcorp.myactivity.dataStore
import com.alcorp.myactivity.databinding.ActivityProfileBinding
import com.alcorp.myactivity.profile.ProfilePreferences
import com.alcorp.myactivity.profile.ProfileThemeViewModel
import com.alcorp.myactivity.profile.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var profileThemeViewModel: ProfileThemeViewModel

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            // Not signed in, hide the sign in button
            binding.signInButton.visibility = View.VISIBLE
            binding.signInReminder.visibility = View.VISIBLE
            binding.logoutBtn.visibility = View.GONE
            binding.logoutBtn.setOnClickListener(null) // Remove the click listener
        } else {
            // Not signed in, hide the logout button
            binding.signInButton.visibility = View.GONE
            binding.signInReminder.visibility = View.GONE
            binding.logoutBtn.visibility = View.VISIBLE
            binding.logoutBtn.setOnClickListener { signOut() }
        }

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

//        binding.logoutBtn.setOnClickListener {
//            signOut()
//        }

        setupViewModels()
        setupObservers()
        setupListeners()
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun setupViewModels() {
        val pref = ProfilePreferences.getInstance(dataStore)
        profileThemeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[ProfileThemeViewModel::class.java]
    }

    private fun setupObservers() {
        profileViewModel.profile.observe(this) { profile ->
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

        profileThemeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
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
            profileThemeViewModel.saveThemeSetting(isChecked)
        }
    }
}