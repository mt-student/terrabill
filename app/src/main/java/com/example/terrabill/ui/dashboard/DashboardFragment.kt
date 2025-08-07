package com.example.terrabill.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terrabill.R
import com.example.terrabill.databinding.FragmentDashboardBinding
import com.google.android.material.tabs.TabLayout

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = JobAdapter { job ->
            findNavController().navigate(
                R.id.action_dashboardFragment_to_timeTrackingFragment,
                bundleOf(
                    "jobId" to job.job.id,
                    "requestId" to job.jobRequestId,
                    "customerId" to job.jobCustomerId,
                    "hourlyRate" to job.job.hourlyRate,
                    "requestedHours" to job.requestedHours
                )
            )
        }
        binding.recyclerViewJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewJobs.adapter = adapter

        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Tag"))
        tabLayout.addTab(tabLayout.newTab().setText("Woche"))
        tabLayout.addTab(tabLayout.newTab().setText("Monat"))
        tabLayout.addTab(tabLayout.newTab().setText("Alle"))

        tabLayout.getTabAt(3)?.select()
        adapter.submitList(viewModel.allJobs())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val filteredJobs = when (tab?.position) {
                    0 -> viewModel.filterJobsByDay()
                    1 -> viewModel.filterJobsByWeek()
                    2 -> viewModel.filterJobsByMonth()
                    else -> viewModel.allJobs()
                }
                adapter.submitList(filteredJobs)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewModel.jobs.observe(viewLifecycleOwner) { jobList ->
            val filteredJobs = when (tabLayout.selectedTabPosition) {
                0 -> viewModel.filterJobsByDay()
                1 -> viewModel.filterJobsByWeek()
                2 -> viewModel.filterJobsByMonth()
                else -> jobList
            }
            adapter.submitList(filteredJobs)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}