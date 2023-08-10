package com.alcorp.myactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alcorp.core.utils.intToBitmap
import com.alcorp.myactivity.adapter.SectionsPagerAdapter
import com.alcorp.myactivity.data.ActivityViewModel
import com.alcorp.myactivity.data.ProfileViewModel
import com.alcorp.myactivity.databinding.ActivityMainBinding
import com.alcorp.myactivity.profile.ProfilePreferences
import com.alcorp.myactivity.profile.ProfileThemeViewModel
import com.alcorp.myactivity.profile.ViewModelFactory
import com.alcorp.myactivity.ui.activity.AddEditActivity
import com.alcorp.myactivity.ui.activity.ProfileActivity
import com.alcorp.myactivity.ui.fragment.CompletedActivityFragment
import com.alcorp.myactivity.ui.fragment.LateFragment
import com.alcorp.myactivity.ui.fragment.TodayActivityFragment
import com.alcorp.myactivity.ui.fragment.UpcomingActivityFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager2
    private val profileViewModel: ProfileViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val pref = ProfilePreferences.getInstance(dataStore)
        val profileThemeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[ProfileThemeViewModel::class.java]

        profileThemeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            setupUI()
        }

    }

    private fun setupUI() {
        // Theme settings are loaded, initialize UI
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, hide the logout button
            binding.logoutBtnMain.visibility = GONE
        }

        binding.logoutBtnMain.setOnClickListener {
            signOut()
            recreate()
        }

        consumeProfileData()

        initializeViews()
        setupViewPager()
        setupTabLayout()

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        setupTabSelectionListener()
        setupPageChangeListener()
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun consumeProfileData() {
        profileViewModel.profile.observe(this) { profile ->
            if (profile != null) {
                binding.greetingMessage.text = getString(R.string.name_main_title, profile.name)
                binding.profileImage.setImageBitmap(intToBitmap(this, profile.photo!!))
            }
        }
    }

    private fun initializeViews() {
        viewPager = binding.viewPagers
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f
    }

    private fun setupViewPager() {
        viewPager = binding.viewPagers
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter
    }

    private fun setupTabLayout() {
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupTabSelectionListener() {
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                updateInfoBox(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupPageChangeListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateInfoBox(position)
            }
        })
    }

    private fun updateInfoBox(position: Int) {
        val fragmentTag = "f$position"
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)

        val activity = when (position) {
            0 -> resources.getString(R.string.today_rectangle_stats)
            1 -> resources.getString(R.string.upcoming_rectangle_stats)
            2 -> resources.getString(R.string.completed_rectangle_stats)
            3 -> resources.getString(R.string.late_rectangle_stats)
            else -> ""
        }

        fragment?.let {
            when (it) {
                is TodayActivityFragment -> {
                    activityViewModel.getCountUncompletedActivityToday.observe(this) { countLeft ->
                        activityViewModel.getCountActivityToday.observe(this) { countTotal ->
                            handleInfoBoxUpdate(activity, countLeft, countTotal)
                        }
                    }
                }

                is UpcomingActivityFragment -> {
                    activityViewModel.getCountUncompletedUpcomingActivity.observe(this) { countLeft ->
                        activityViewModel.getCountUpcomingActivity.observe(this) { countTotal ->
                            handleInfoBoxUpdate(activity, countLeft, countTotal)
                        }
                    }
                }

                is CompletedActivityFragment -> {
                    activityViewModel.getCountCompletedActivity.observe(this) { countTotal ->
                        handleCompletedInfoBoxUpdate(activity, countTotal)
                    }
                }

                is LateFragment -> {
                    activityViewModel.getCountLateActivity.observe(this) { countTotal ->
                        handleLateInfoBoxUpdate(activity, countTotal)
                    }
                }
            }
        }
    }

    private fun handleInfoBoxUpdate(activity: String, countLeft: Int, countTotal: Int) {
        binding.DayActivity.text = activity
        if (countTotal == 0) {
            binding.DescDayActivity.text = resources.getString(R.string.today_noTask_stats)
        } else if (countLeft == 0) {
            binding.DescDayActivity.text =
                resources.getString(R.string.today_detailed_completed_all_stats, countTotal)
        } else {
            binding.DescDayActivity.text = resources.getString(
                R.string.today_detailed_uncompleted_stats,
                countLeft,
                countTotal
            )
        }

        val progress = if (countTotal > 0) (((countTotal - countLeft) * 100) / countTotal) else 0
        binding.percentageText.text = resources.getString(R.string.percentage_stats, progress)
        binding.percentageBar.progress = progress
    }

    private fun handleCompletedInfoBoxUpdate(activity: String, countTotal: Int) {
        binding.DayActivity.text = activity
        if (countTotal == 0) {
            binding.DescDayActivity.text =
                resources.getString(R.string.today_noTask_completed_stats)
            binding.percentageText.text = resources.getString(R.string.percentage_stats, 0)
            binding.percentageBar.progress = 0
        } else {
            binding.DescDayActivity.text =
                resources.getString(R.string.today_detailed_completed_stats, countTotal)
            binding.percentageText.text = resources.getString(R.string.percentage_stats, 100)
            binding.percentageBar.progress = 100
        }
    }

    private fun handleLateInfoBoxUpdate(activity: String, countTotal: Int) {
        binding.DayActivity.text = activity
        if (countTotal == 0) {
            binding.DescDayActivity.text = resources.getString(R.string.today_noTask_late_stats)
            binding.percentageText.text = resources.getString(R.string.percentage_stats, 0)
            binding.percentageBar.progress = 0
        } else {
            binding.DescDayActivity.text =
                resources.getString(R.string.today_detailed_late_stats, countTotal)
            binding.percentageText.text = resources.getString(R.string.percentage_stats, 100)
            binding.percentageBar.progress = 100
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.today_activity,
            R.string.upcoming_activity,
            R.string.completed_activity,
            R.string.late_activity
        )
    }
}