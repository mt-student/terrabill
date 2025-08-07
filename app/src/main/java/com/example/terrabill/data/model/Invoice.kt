package com.example.terrabill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val createdAt: String,
    val timeTracked: Long, // seconds
    val amount: Double,
    val requestId: String,
    val customerId: String,
    val jobId: String
)