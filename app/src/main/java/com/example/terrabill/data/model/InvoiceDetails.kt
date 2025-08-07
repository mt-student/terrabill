package com.example.terrabill.data.model

import androidx.room.Embedded

data class InvoiceDetails(
    @Embedded val invoice: Invoice,
    val customerFirstname: String,
    val customerLastname: String,
    val customerOrganization: String?,
    val customerStreet: String,
    val customerHouseNumber: String,
    val customerPostcode: String,
    val customerCity: String,
    val customerPhone: String,
    val jobStartAt: String,
    val jobHourlyRate: Double,
    val requestDescription: String
)

fun InvoiceDetails.getTitle(): String {
    return if (!customerOrganization.isNullOrBlank()) "${invoice.createdAt} - $customerOrganization"
    else "${invoice.createdAt} - $customerFirstname $customerLastname"
}
