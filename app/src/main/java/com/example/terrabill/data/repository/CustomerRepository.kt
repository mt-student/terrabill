package com.example.terrabill.data.repository

import com.example.terrabill.data.local.CustomerDao
import com.example.terrabill.data.model.Customer
import kotlinx.coroutines.flow.Flow

class CustomerRepository(private val dao: CustomerDao) {

    fun getAllCustomer(): Flow<List<Customer>> = dao.getAllCustomers()
    suspend fun findByNameOrOrganization(
        firstname: String,
        lastname: String,
        organization: String?
    ): Customer? {
        return dao.findCustomerByNameOrOrganization(firstname, lastname, organization)
    }

    suspend fun insert(customer: Customer) {
        return dao.insert(customer)
    }

    suspend fun delete(customer: Customer) = dao.delete(customer)
    suspend fun update(customer: Customer) {
        dao.update(customer)
    }
}