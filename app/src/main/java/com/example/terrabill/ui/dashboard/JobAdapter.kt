package com.example.terrabill.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.terrabill.data.model.JobDetails
import com.example.terrabill.databinding.ItemJobBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri

class JobAdapter(
    private val onJobClick: (JobDetails) -> Unit
) : ListAdapter<JobDetails, JobAdapter.JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding, onJobClick)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class JobViewHolder(
        private val binding: ItemJobBinding,
        private val onJobClick: (JobDetails) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(job: JobDetails) {
            val dateTime = LocalDateTime.parse(job.job.startAt)
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val endTime = dateTime.plusHours(job.requestedHours.toLong())

            binding.textCustomerName.text = listOfNotNull(
                "${job.customerFirstname} ${job.customerLastname}".takeIf { it.isNotBlank() },
                job.customerOrganization
            ).joinToString(" | ")

            binding.textJobTime.text = "Zeit: ${dateTime.format(formatter)} - ${
                endTime.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            } Uhr"
            binding.textJobAddress.text =
                "${job.customerStreet} ${job.customerHouseNumber}, ${job.customerPostcode} ${job.customerCity}"
            binding.textJobDescription.text = "Aufgabe: ${job.requestDescription}"

            binding.buttonCall.setOnClickListener {
                val phone = job.customerPhone ?: ""
                val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:$phone".toUri()
                }
                it.context.startActivity(phoneIntent)
            }

            binding.buttonNavigate.setOnClickListener {
                val address =
                    "${job.customerStreet} ${job.customerHouseNumber}, ${job.customerPostcode} ${job.customerCity}"
                val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                it.context.startActivity(mapIntent)
            }

            binding.root.setOnClickListener {
                onJobClick(job)
            }
        }
    }

    class JobDiffCallback : DiffUtil.ItemCallback<JobDetails>() {
        override fun areItemsTheSame(oldItem: JobDetails, newItem: JobDetails): Boolean =
            oldItem.job.id == newItem.job.id

        override fun areContentsTheSame(oldItem: JobDetails, newItem: JobDetails): Boolean =
            oldItem == newItem
    }
}