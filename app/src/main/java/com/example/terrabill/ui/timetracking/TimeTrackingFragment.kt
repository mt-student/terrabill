package com.example.terrabill.ui.timetracking

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.terrabill.data.local.DatabaseProvider
import com.example.terrabill.data.model.Invoice
import com.example.terrabill.data.model.JobStatus
import com.example.terrabill.databinding.FragmentTimeTrackingBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeTrackingFragment : Fragment() {

    private var _binding: FragmentTimeTrackingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TimeTrackingViewModel

    private var jobId: String = ""
    private var customerId: String = ""
    private var requestId: String = ""
    private var hourlyRate: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeTrackingBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TimeTrackingViewModel::class.java]

        arguments?.let {
            jobId = it.getString("jobId", "")
            customerId = it.getString("customerId", "")
            requestId = it.getString("requestId", "")
            hourlyRate = it.getDouble("hourlyRate", 0.0)
        }

        viewModel.seconds.observe(viewLifecycleOwner) { secs ->
            binding.textTimer.text = formatTime(secs)
        }

        binding.buttonStart.setOnClickListener {
            changeJobStatus(JobStatus.GESTARTET)
            viewModel.startTimer()
        }

        binding.buttonPause.setOnClickListener {
            viewModel.pauseTimer()
        }

        binding.buttonStop.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Beenden?")
                .setMessage("Möchten Sie die Zeiterfassung wirklich beenden?")
                .setPositiveButton("Ja") { _, _ ->
                    viewModel.stopTimer()
                    saveInvoice(viewModel.seconds.value ?: 0)
                    changeJobStatus(JobStatus.ABGESCHLOSSEN)
                    findNavController().popBackStack()
                }
                .setNegativeButton("Nein", null)
                .show()
        }

        return binding.root
    }

    private fun saveInvoice(seconds: Long) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
        val createdAt = LocalDateTime.now().format(formatter)
        val amount = (seconds / 3600.0) * hourlyRate

        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            val invoice = Invoice(
                createdAt = createdAt,
                timeTracked = seconds,
                amount = amount,
                requestId = requestId,
                customerId = customerId,
                jobId = jobId
            )
            db.invoiceDao().create(invoice)
        }
    }

    private fun changeJobStatus(jobStatus: JobStatus) {
        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            db.jobDao().updateStatus(jobId, jobStatus)
        }
    }

    private fun formatTime(seconds: Long): String {
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hrs, mins, secs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}