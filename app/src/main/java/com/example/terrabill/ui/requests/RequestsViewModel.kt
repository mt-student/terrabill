package com.example.terrabill.ui.requests

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.terrabill.data.local.DatabaseProvider
import com.example.terrabill.data.model.Customer
import com.example.terrabill.data.model.Job
import com.example.terrabill.data.model.JobStatus
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.RequestStatus
import com.example.terrabill.data.repository.CustomerRepository
import com.example.terrabill.data.repository.JobRepository
import com.example.terrabill.data.repository.RequestRepository
import kotlinx.coroutines.launch

class RequestsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)
    private val requestRepository = RequestRepository(db.requestDao())
    private val jobRepository = JobRepository(db.jobDao())
    private val customerRepository = CustomerRepository(db.customerDao())

    val request = requestRepository.getRequestsByStatus(RequestStatus.OFFEN).asLiveData()
    fun accept(request: Request, hourlyRate: Double, customerId: String) {
        viewModelScope.launch {
            requestRepository.changeStatus(request.id, RequestStatus.ANGENOMMEN)

            val job = Job(
                requestId = request.id,
                customerId = customerId.toString(),
                hourlyRate = hourlyRate,
                startAt = request.dateTime,
                status = JobStatus.OFFEN
            )
            jobRepository.create(job)
        }
    }

    fun decline(request: Request) {
        viewModelScope.launch {
            requestRepository.changeStatus(request.id, RequestStatus.ABGELEHNT)
        }
    }

    fun getCustomerForRequest(request: Request, callback: (Customer?) -> Unit) {
        viewModelScope.launch {
            val customer = customerRepository.findByNameOrOrganization(
                request.firstname,
                request.lastname,
                request.organization
            )
            callback(customer)
        }
    }

    fun createCustomerAndAccept(request: Request, customer: Customer, hourlyRate: Double) {
        viewModelScope.launch {
            customerRepository.insert(customer)
            accept(request, hourlyRate, customer.id)
        }
    }
}