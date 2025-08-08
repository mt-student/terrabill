package com.example.terrabill.ui.dashboard

import android.os.Bundle
import android.util.Log
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
import android.widget.CalendarView
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentTabText: String = "Alle"

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

        val calendarView: CalendarView = binding.calendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selected = LocalDate.of(year, month + 1, dayOfMonth)
            val filtered = viewModel.allJobs().filter { j ->
                runCatching { LocalDateTime.parse(j.job.startAt) }
                    .getOrNull()
                    ?.toLocalDate() == selected
            }
            adapter.submitList(filtered)
        }

        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Tag"))
        tabLayout.addTab(tabLayout.newTab().setText("Woche"))
        tabLayout.addTab(tabLayout.newTab().setText("Monat"))
        tabLayout.addTab(tabLayout.newTab().setText("Alle"))
        tabLayout.addTab(tabLayout.newTab().setText("Kalender"))

        tabLayout.getTabAt(3)?.select()
        adapter.submitList(viewModel.allJobs())

        fun renderForTab(tabText: String, viewModel: DashboardViewModel, adapter: JobAdapter) {
            currentTabText = tabText
            when (tabText) {
                "Tag" -> {
                    binding.calendarView.visibility = View.GONE
                    adapter.submitList(viewModel.filterJobsByDay())
                }
                "Woche" -> {
                    binding.calendarView.visibility = View.GONE
                    adapter.submitList(viewModel.filterJobsByWeek())
                }
                "Monat" -> {
                    binding.calendarView.visibility = View.GONE
                    adapter.submitList(viewModel.filterJobsByMonth())
                }
                "Kalender" -> {
                    binding.calendarView.visibility = View.VISIBLE
                    val selected = Instant.ofEpochMilli(binding.calendarView.date)
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                    adapter.submitList(viewModel.jobsOn(selected))
                }
                else -> { // Alle
                    binding.calendarView.visibility = View.GONE
                    adapter.submitList(viewModel.allJobs())
                }
            }
            Log.d("Dashboard", "renderForTab -> $tabText, Calendar visibility=${binding.calendarView.visibility}")
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val text = tab?.text?.toString() ?: "Alle"
                renderForTab(text, viewModel, adapter)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val text = tab?.text?.toString() ?: currentTabText
                renderForTab(text, viewModel, adapter)
            }
        })

        viewModel.jobs.observe(viewLifecycleOwner) {
            val activeTabText = tabLayout.getTabAt(tabLayout.selectedTabPosition)?.text?.toString() ?: currentTabText
            renderForTab(activeTabText, viewModel, adapter)

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}