package com.example.terrabill.ui.invoices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terrabill.R
import com.example.terrabill.R.*
import com.example.terrabill.databinding.FragmentInvoicesBinding
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class InvoicesFragment : Fragment() {
    private var _binding: FragmentInvoicesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InvoicesAdapter
    private val viewModel: InvoicesViewModel by viewModels()

    @SuppressLint("MissingInflatedId", "SetTextI18n", "DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoicesBinding.inflate(inflater, container, false)

        adapter = InvoicesAdapter(
            listOf(),
            onClick = { invoiceItem ->
                val context = requireContext()
                val invoice = invoiceItem.invoice
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

                val dialogView = LayoutInflater.from(context).inflate(layout.dialog_invoice, null)

                val tvTitle = dialogView.findViewById<TextView>(R.id.tvInvoiceTitle)
                val tvLeftInfo = dialogView.findViewById<TextView>(R.id.tvInvoiceAddressLeft)
                val tvRightInfo = dialogView.findViewById<TextView>(R.id.tvInvoiceAddressRight)
                val tvCalculation = dialogView.findViewById<TextView>(R.id.tvInvoiceCalculation)

                tvTitle.text = "Rechnung für ${invoiceItem.requestDescription}"
                tvLeftInfo.text =
                    "${invoiceItem.customerFirstname} ${invoiceItem.customerLastname}\n${invoiceItem.customerStreet} ${invoiceItem.customerHouseNumber}\n${invoiceItem.customerPostcode} ${invoiceItem.customerCity}"
                tvRightInfo.text = "Rechnung erstellt: ${invoice.createdAt.format(formatter)}"
                tvCalculation.text =
                    "${
                        String.format(
                            "%.4f",
                            invoiceItem.invoice.timeTracked / 3600f
                        )
                    } Std x ${String.format("%.2f", invoiceItem.jobHourlyRate)} € = ${
                        String.format(
                            "%.2f",
                            invoice.amount
                        )
                    } €"
                AlertDialog.Builder(context)
                    .setTitle("Rechnung")
                    .setView(dialogView)
                    .setPositiveButton("Schließen", null)
                    .show()
            },
            onDelete = {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteInvoice(it.invoice)
                }
            }
        )

        binding.recyclerViewInvoices.adapter = adapter
        binding.recyclerViewInvoices.layoutManager = LinearLayoutManager(requireContext())

        viewModel.invoices.observe(viewLifecycleOwner) { invoices ->
            adapter.updateData(invoices)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}