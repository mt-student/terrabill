package com.example.terrabill.ui.customers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terrabill.databinding.FragmentCustomersBinding
import kotlinx.coroutines.launch

class CustomersFragment : Fragment() {

    private var _binding: FragmentCustomersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val customersViewModel = ViewModelProvider(this).get(CustomersViewModel::class.java)

        _binding = FragmentCustomersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = CustomerAdapter(
            onUpdateCustomer = { updatedCustomer ->
                lifecycleScope.launch {
                    customersViewModel.updateCustomer(updatedCustomer)
                }
            },
            onDeleteCustomer = { customerToDelete ->
                lifecycleScope.launch {
                    customersViewModel.deleteCustomer(customerToDelete)
                }
            }
        )
        binding.recyclerViewCustomers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCustomers.adapter = adapter

        customersViewModel.customers.observe(viewLifecycleOwner) { customerList ->
            adapter.submitList(customerList)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}