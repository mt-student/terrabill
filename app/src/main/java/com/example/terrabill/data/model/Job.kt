package com.example.terrabill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val requestId: String,
    val customerId: String,
    val hourlyRate: Double,
    val startAt: String,
    val status: JobStatus = JobStatus.OFFEN
)