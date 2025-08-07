package com.example.terrabill.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.terrabill.data.model.Customer
import com.example.terrabill.databinding.FragmentRequestsBinding

class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RequestAdapter
    private lateinit var viewModel: RequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RequestsViewModel::class.java]

        setupRecyclerView()
        observeData()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = RequestAdapter(
            onAccept = { request ->
                AcceptRequestDialog(
                    request,
                    onCustomerSelected = { customer: Customer ->
                        // Falls Kunde schon in DB vorhanden → Job direkt anlegen
                        viewModel.accept(request, hourlyRate = 25.0, customerId = customer.id)
                    },
                    onNewCustomerCreated = { newCustomer: Customer ->
                        // Neuen Kunden in DB anlegen und dann Job erstellen
                        viewModel.createCustomerAndAccept(request, newCustomer, hourlyRate = 25.0)
                    }
                ).show(parentFragmentManager, "AcceptDialog")
            },
            onDecline = { request ->
                viewModel.decline(request)
            }
        )
        binding.recyclerRequests.adapter = adapter
    }

    private fun observeData() {
        viewModel.request.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}