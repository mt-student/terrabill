package com.example.terrabill.data.local

import androidx.room.*
import com.example.terrabill.data.model.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customers ORDER BY lastname")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE organization= :organization")
    fun getCustomersByOrganization(organization: String): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :customerId")
    fun getCustomerById(customerId: String): Customer

    @Query(
        """
    SELECT * FROM customers
    WHERE (firstname = :firstName AND lastname = :lastName)
       OR (organization IS NOT NULL AND organization = :organization)
    LIMIT 1
"""
    )
    suspend fun findCustomerByNameOrOrganization(
        firstName: String,
        lastName: String,
        organization: String?
    ): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Delete
    suspend fun delete(customer: Customer)
}