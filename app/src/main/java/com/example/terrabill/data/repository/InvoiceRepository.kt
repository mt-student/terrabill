package com.example.terrabill.data.repository

import com.example.terrabill.data.local.InvoiceDao
import com.example.terrabill.data.model.Invoice
import com.example.terrabill.data.model.InvoiceDetails
import kotlinx.coroutines.flow.Flow

class InvoiceRepository(private val dao: InvoiceDao) {

    fun getInvoiceDetails(): Flow<List<InvoiceDetails>> {
        return dao.getInvoiceDetails()
    }

    suspend fun deleteInvoice(invoice: Invoice) {
        dao.delete(invoice)
    }
}