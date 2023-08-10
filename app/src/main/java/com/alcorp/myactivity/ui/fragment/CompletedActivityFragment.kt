package com.alcorp.myactivity.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.ui.ActivityAdapter
import com.alcorp.myactivity.data.ActivityViewModel
import com.alcorp.myactivity.databinding.FragmentCompletedActivityBinding
import com.alcorp.myactivity.ui.activity.AddEditActivity
import com.alcorp.myactivity.ui.activity.DetailCompletedActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedActivityFragment : Fragment() {
    private var _binding: FragmentCompletedActivityBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val activityAdapter = ActivityAdapter()
            activityAdapter.onItemClick = { selectedData ->
                navigateToCorrectActivity(selectedData)
            }

            activityViewModel.getCompletedActivity.observe(viewLifecycleOwner) { activityList ->
                if (activityList.isEmpty()) {
                    binding.dataNoneCompletedActivity.visibility = View.VISIBLE
                } else {
                    binding.dataNoneCompletedActivity.visibility = View.GONE
                    activityAdapter.submitList(activityList)
                }
            }

            val recyclerView = binding.rvCompletedActivity
            with(recyclerView) {
                this.layoutManager = LinearLayoutManager(requireContext())
                this.setHasFixedSize(true)
                this.adapter = activityAdapter
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