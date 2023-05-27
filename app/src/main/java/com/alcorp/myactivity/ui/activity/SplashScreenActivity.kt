package com.alcorp.myactivity.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.alcorp.myactivity.database.preferences.SettingsPreferences
import com.alcorp.myactivity.database.viewmodel.MainViewModel
import com.alcorp.myactivity.database.viewmodel.ViewModelFactory
import com.alcorp.myactivity.databinding.ActivitySplashScreenBinding

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val i = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000) // Waktu delay dalam milidetik sebelum pindah ke MainActivity

    }
}