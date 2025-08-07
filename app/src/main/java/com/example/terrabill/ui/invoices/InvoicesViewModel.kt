package com.example.terrabill.ui.invoices

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.terrabill.data.local.DatabaseProvider
import com.example.terrabill.data.model.Invoice
import com.example.terrabill.data.model.InvoiceDetails
import com.example.terrabill.data.repository.InvoiceRepository
import java.time.LocalDate

class InvoicesViewModel(application: Application) : AndroidViewModel(application) {

    private val invoiceDao = DatabaseProvider.getDatabase(application).invoiceDao()
    private val invoiceRepository = InvoiceRepository(invoiceDao)

    val invoices: LiveData<List<InvoiceDetails>> = invoiceRepository
        .getInvoiceDetails()
        .asLiveData()

    fun filterInvoices(): List<InvoiceDetails> {
        val today = LocalDate.now()
        return invoices.value?.filter {
            LocalDate.parse(it.invoice.createdAt.substring(0, 10)) == today
        } ?: emptyList()
    }

    suspend fun deleteInvoice(invoice: Invoice) {
        invoiceRepository.deleteInvoice(invoice)
    }
}