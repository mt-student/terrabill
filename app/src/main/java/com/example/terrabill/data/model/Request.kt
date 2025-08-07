package com.example.terrabill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "requests")
data class Request(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val firstname: String,
    val lastname: String,
    val organization: String? = null,

    val street: String,
    val houseNumber: String,
    val postcode: String,
    val city: String,

    val phone: String,

    val dateTime: String,
    val description: String,
    val requestedHours: Int,
    val status: RequestStatus
)

fun Request.getDisplayName(): String {
    return if (!organization.isNullOrBlank()) "$firstname $lastname (${organization})"
    else "$firstname $lastname"
}

fun Request.getFullAddress(): String {
    return "$street $houseNumber, $postcode $city"
}

fun Request.toCustomer(): Customer = Customer(
    firstname = firstname,
    lastname = lastname,
    organization = organization,
    street = street,
    houseNumber = houseNumber,
    postcode = postcode,
    city = city,
    phone = phone
)