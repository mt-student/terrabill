package com.example.terrabill.ui.requests

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.getDisplayName
import com.example.terrabill.data.model.getFullAddress
import com.example.terrabill.databinding.ItemRequestBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RequestAdapter(
    private val onAccept: (Request) -> Unit,
    private val onDecline: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private var requests: List<Request> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Request>) {
        requests = newList
        notifyDataSetChanged()
    }

    inner class RequestViewHolder(val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val item = requests[position]
        holder.binding.textName.text = item.getDisplayName()
        holder.binding.textAddress.text = item.getFullAddress()
        holder.binding.textDateTime.text = formatDateTime(item.dateTime)
        holder.binding.textDescription.text = item.description
        holder.binding.textRequestedHours.text = "Gewünschte Dauer: ${item.requestedHours} Stunden"
        holder.binding.buttonAccept.setOnClickListener { onAccept(item) }
        holder.binding.buttonDecline.setOnClickListener { onDecline(item) }
    }

    override fun getItemCount() = requests.size

    private fun formatDateTime(dateTime: String): String {
        val parsed = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        return parsed.format(formatter)
    }
}