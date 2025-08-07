package com.example.terrabill.ui.invoices

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.terrabill.data.model.Invoice
import com.example.terrabill.data.model.InvoiceDetails
import com.example.terrabill.data.model.getTitle
import com.example.terrabill.databinding.ItemInvoiceBinding
import java.text.NumberFormat
import java.util.Locale
import kotlin.text.*

class InvoicesAdapter(
    private var items: List<InvoiceDetails>,
    private val onClick: (InvoiceDetails) -> Unit,
    private val onDelete: (InvoiceDetails) -> Unit
) : RecyclerView.Adapter<InvoicesAdapter.InvoiceViewHolder>() {

    inner class InvoiceViewHolder(val binding: ItemInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(invoice: InvoiceDetails) {
            binding.textInvoiceTitle.text = invoice.getTitle()
            binding.textInvoiceAmount.text =
                NumberFormat.getCurrencyInstance(Locale.GERMANY).format(invoice.invoice.amount)

            val hours = invoice.invoice.timeTracked / 3600
            val minutes = (invoice.invoice.timeTracked % 3600) / 60
            binding.textInvoiceTime.text = String.format("%02d:%02d h", hours, minutes)
            binding.textInvoiceRequestDescription.text = invoice.requestDescription

            binding.root.setOnClickListener { onClick(invoice) }
            binding.buttonDelete.setOnClickListener { onDelete(invoice) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<InvoiceDetails>) {
        items = newItems
        notifyDataSetChanged()
    }
}