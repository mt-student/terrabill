package com.example.terrabill.data.model

import androidx.room.Embedded

data class JobDetails(
    @Embedded val job: Job,
    val jobRequestId: String,
    val jobCustomerId: String,
    val requestDescription: String,
    val requestedHours: Int,
    val customerFirstname: String?,
    val customerLastname: String?,
    val customerOrganization: String?,
    val customerStreet: String?,
    val customerHouseNumber: String?,
    val customerPostcode: String?,
    val customerCity: String?,
    val customerPhone: String?
)