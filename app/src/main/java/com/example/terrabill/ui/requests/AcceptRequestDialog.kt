package com.example.terrabill.ui.requests

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.terrabill.R
import com.example.terrabill.data.model.Customer
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.toCustomer
import com.example.terrabill.data.http.GPT

class AcceptRequestDialog(
    private val request: Request,
    private val onCustomerSelected: (Customer) -> Unit,
    private val onNewCustomerCreated: (Customer) -> Unit
) : DialogFragment() {

    private val viewModel: RequestsViewModel by viewModels()

    @SuppressLint("MissingInflatedId", "UseGetLayoutInflater", "SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_accept_request, null)
        val tvCustomerInfo = view.findViewById<TextView>(R.id.tvCustomerInfo)
        val inputHourlyRate = view.findViewById<EditText>(R.id.inputHourlyRate)
        val tvHourlyRateSuggestion = view.findViewById<TextView>(R.id.tvHourlyRateSuggestion)
        val btnUseExisting = view.findViewById<Button>(R.id.btnUseExisting)
        val btnCreateNew = view.findViewById<Button>(R.id.btnCreateNew)

        GPT().suggestHourlyRate( request.city, request.street) { suggestion ->
            requireActivity().runOnUiThread {
                tvHourlyRateSuggestion.text = suggestion
            }
        }

        viewModel.getCustomerForRequest(request) { existingCustomer ->
            if (existingCustomer != null) {
                tvCustomerInfo.text = """
                    ${existingCustomer.firstname} ${existingCustomer.lastname}
                    ${existingCustomer.organization ?: ""}
                    ${existingCustomer.street} ${existingCustomer.houseNumber}
                    ${existingCustomer.postcode} ${existingCustomer.city}
                """.trimIndent()

                btnUseExisting.isEnabled = true
                btnUseExisting.setOnClickListener {
                    onCustomerSelected(existingCustomer)
                    dismiss()
                }

                btnCreateNew.setOnClickListener {
                    val newCustomer = request.toCustomer()
                    onNewCustomerCreated(newCustomer)
                    dismiss()
                }
            } else {
                tvCustomerInfo.text = """
                    ${request.firstname} ${request.lastname}
                    ${request.organization ?: ""}
                    ${request.street} ${request.houseNumber}
                    ${request.postcode} ${request.city}
                """.trimIndent()

                btnUseExisting.isEnabled = false
                btnCreateNew.setOnClickListener {
                    val newCustomer = request.toCustomer()
                    onNewCustomerCreated(newCustomer)
                    dismiss()
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Auftrag annehmen")
            .setView(view)
            .setNegativeButton("Abbrechen", null)
            .create()
    }
}