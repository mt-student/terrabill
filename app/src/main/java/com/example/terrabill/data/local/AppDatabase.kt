package com.example.terrabill.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.terrabill.data.model.Job
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.Customer
import com.example.terrabill.data.model.Invoice

@Database(
    entities = [Request::class, Job::class, Customer::class, Invoice::class],
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestDao(): RequestDao
    abstract fun jobDao(): JobDao
    abstract fun customerDao(): CustomerDao
    abstract fun invoiceDao(): InvoiceDao
}