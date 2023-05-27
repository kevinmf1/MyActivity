package com.alcorp.myactivity.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.alcorp.myactivity.R
import com.alcorp.myactivity.adapter.SectionsPagerAdapter
import com.alcorp.myactivity.database.repository.ActivityRepository
import com.alcorp.myactivity.database.repository.ProfileRepository
import com.alcorp.myactivity.databinding.ActivityMainBinding
import com.alcorp.myactivity.tools.intToBitmap
import com.alcorp.myactivity.ui.fragment.CompletedActivityFragment
import com.alcorp.myactivity.ui.fragment.LateFragment
import com.alcorp.myactivity.ui.fragment.TodayActivityFragment
import com.alcorp.myactivity.ui.fragment.UpcomingActivityFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var activityRepository: ActivityRepository
    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActivityRepository()
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

    private fun setupActivityRepository() {
        activityRepository = ActivityRepository(application)
        profileRepository = ProfileRepository(application)
    }

    private fun consumeProfileData() {
        profileRepository.getProfile().observe(this) { profile ->
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
                    activityRepository.getCountUncompletedActivityToday().observe(this) { countLeft ->
                        activityRepository.getCountActivityToday().observe(this) { countTotal ->
                            handleInfoBoxUpdate(activity, countLeft, countTotal)
                        }
                    }
                }
                is UpcomingActivityFragment -> {
                    activityRepository.getCountUncompletedUpcomingActivity().observe(this) { countLeft ->
                        activityRepository.getCountUpcomingActivity().observe(this) { countTotal ->
                            handleInfoBoxUpdate(activity, countLeft, countTotal)
                        }
                    }
                }
                is CompletedActivityFragment -> {
                    activityRepository.getCountCompletedActivity().observe(this) { countTotal ->
                        handleCompletedInfoBoxUpdate(activity, countTotal)
                    }
                }
                is LateFragment -> {
                    activityRepository.getCountLateActivity().observe(this) { countTotal ->
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
            binding.DescDayActivity.text = resources.getString(R.string.today_detailed_completed_all_stats, countTotal)
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
            binding.DescDayActivity.text = resources.getString(R.string.today_noTask_completed_stats)
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