package com.alcorp.myactivity.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.myactivity.adapter.ActivityAdapter
import com.alcorp.myactivity.database.repository.ActivityRepository
import com.alcorp.myactivity.databinding.FragmentCompletedActivityBinding

class CompletedActivityFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ActivityAdapter? = null
    private lateinit var mActivityRepository: ActivityRepository
    private lateinit var binding: FragmentCompletedActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.rvCompletedActivity
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter

        mActivityRepository = ActivityRepository(requireActivity().application)
        mActivityRepository.getCompletedActivity().observe(viewLifecycleOwner) { activityList ->
            if (activityList.isEmpty()) {
                binding.dataNoneCompletedActivity.visibility = View.VISIBLE
            } else {
                binding.dataNoneCompletedActivity.visibility = View.GONE
                adapter = ActivityAdapter(activityList, requireActivity())
                recyclerView?.adapter = adapter
            }
        }
    }
}