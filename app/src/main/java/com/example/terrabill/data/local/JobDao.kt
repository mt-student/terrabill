package com.example.terrabill.data.local

import androidx.room.*
import com.example.terrabill.data.model.Job
import com.example.terrabill.data.model.JobDetails
import com.example.terrabill.data.model.JobStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {

    @Query("SELECT * FROM jobs")
    fun getAllJobs(): Flow<List<Job>>

    @Query("SELECT * FROM jobs WHERE status= :status")
    fun getJobsByStatus(status: JobStatus): Flow<List<Job>>

    @Query("SELECT * FROM jobs ORDER BY startAt DESC")
    fun getAllJobsSortedByDate(): Flow<List<Job>>

    @Query(
        """
    SELECT 
        jobs.*, 
        requests.description AS requestDescription, 
        requests.requestedHours AS requestedHours,
        customers.id AS jobCustomerId,
        requests.id AS jobRequestId,
        customers.firstname AS customerFirstname,
        customers.lastname AS customerLastname,
        customers.organization AS customerOrganization,
        customers.street AS customerStreet,
        customers.houseNumber AS customerHouseNumber,
        customers.postcode AS customerPostcode,
        customers.city AS customerCity,
        customers.phone AS customerPhone
    FROM jobs
    JOIN requests ON jobs.requestId = requests.id
    JOIN customers ON jobs.customerId = customers.id
    ORDER BY jobs.startAt ASC
"""
    )
    fun getJobDetails(): Flow<List<JobDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: Job)

    @Delete
    suspend fun delete(job: Job)

    @Query("UPDATE jobs SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: JobStatus)

    @Query("SELECT * FROM jobs WHERE id = :jobId")
    suspend fun getJobById(jobId: String): Job
}