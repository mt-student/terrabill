package com.example.terrabill.ui.customers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.terrabill.data.local.DatabaseProvider
import com.example.terrabill.data.model.Customer
import com.example.terrabill.data.repository.CustomerRepository
import kotlinx.coroutines.launch

class CustomersViewModel(application: Application) : AndroidViewModel(application) {
    private val customerDao = DatabaseProvider.getDatabase(application).customerDao()
    private val repository = CustomerRepository(customerDao)

    val customers = repository.getAllCustomer().asLiveData()
    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.update(customer)
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.delete(customer)
        }
    }
}