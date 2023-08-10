package com.alcorp.myactivity.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.ui.ActivityAdapter
import com.alcorp.myactivity.data.ActivityViewModel
import com.alcorp.myactivity.databinding.FragmentLateBinding
import com.alcorp.myactivity.ui.activity.AddEditActivity
import com.alcorp.myactivity.ui.activity.DetailCompletedActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LateFragment : Fragment() {
    private var _binding: FragmentLateBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (activity != null) {
            val adapter = ActivityAdapter()
            adapter.onItemClick = { selectedData ->
                navigateToCorrectActivity(selectedData)
            }

            activityViewModel.getLateActivity.observe(viewLifecycleOwner) { activityList ->
                if (activityList.isEmpty()) {
                    binding.dataNoneLateActivity.visibility = View.VISIBLE
                } else {
                    binding.dataNoneLateActivity.visibility = View.GONE
                    adapter.submitList(activityList)
                }
            }

            val recyclerView = binding.rvLateActivity
            with(recyclerView) {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.setHasFixedSize(true)
                this.adapter = adapter
            }
        }
    }

    private fun navigateToCorrectActivity(activity: ActivityEntity) {
        val targetActivityClass = if (activity.isDone) {
            DetailCompletedActivity::class.java
        } else {
            AddEditActivity::class.java
        }

        val intent = Intent(requireContext(), targetActivityClass)
        intent.putExtra("id", activity.id)
        intent.putExtra("date", activity.date)
        intent.putExtra("timeStart", activity.timeStart)
        intent.putExtra("timeEnd", activity.timeEnd)
        intent.putExtra("title", activity.title)
        intent.putExtra("desc", activity.desc)
        intent.putExtra("isDone", activity.isDone)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}