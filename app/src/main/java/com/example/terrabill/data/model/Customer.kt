package com.example.terrabill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val firstname: String,
    val lastname: String,
    val organization: String? = null,
    val street: String,
    val houseNumber: String,
    val postcode: String,
    val city: String,
    val phone: String
)