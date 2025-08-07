package com.example.terrabill.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.terrabill.data.model.Invoice
import com.example.terrabill.data.model.InvoiceDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY rowid DESC")
    fun getAllInvoices(): Flow<List<Invoice>>

    @Query("""
    SELECT 
        invoices.*,
        customers.firstname AS customerFirstname,
        customers.lastname AS customerLastname,
        customers.organization AS customerOrganization,
        customers.street AS customerStreet,
        customers.houseNumber AS customerHouseNumber,
        customers.postcode AS customerPostcode,
        customers.city AS customerCity,
        customers.phone AS customerPhone,
        jobs.startAt AS jobStartAt,
        jobs.hourlyRate AS jobHourlyRate,
        requests.description AS requestDescription
    FROM invoices
    JOIN jobs ON invoices.jobId = jobs.id
    JOIN customers ON invoices.customerId = customers.id
    JOIN requests ON invoices.requestId = requests.id
    ORDER BY invoices.createdAt DESC
""")
    fun getInvoiceDetails(): Flow<List<InvoiceDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(invoice: Invoice)

    @Delete
    suspend fun delete(invoice: Invoice)

    @Query("SELECT * FROM invoices WHERE id = :id")
    suspend fun getById(id: String): Invoice?
}