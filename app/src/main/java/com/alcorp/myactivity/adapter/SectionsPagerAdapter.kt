package com.alcorp.myactivity.adapter

import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alcorp.myactivity.ui.fragment.CompletedActivityFragment
import com.alcorp.myactivity.ui.fragment.LateFragment
import com.alcorp.myactivity.ui.fragment.TodayActivityFragment
import com.alcorp.myactivity.ui.fragment.UpcomingActivityFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val fragmentSparseArray = SparseArray<Fragment>()

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> TodayActivityFragment()
            1 -> UpcomingActivityFragment()
            2 -> CompletedActivityFragment()
            3 -> LateFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
        fragmentSparseArray.put(position, fragment)
        return fragment
    }

    override fun getItemCount(): Int = 4
}