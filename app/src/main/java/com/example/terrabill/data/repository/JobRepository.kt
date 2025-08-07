package com.example.terrabill.data.repository

import com.example.terrabill.data.local.JobDao
import com.example.terrabill.data.model.Job
import com.example.terrabill.data.model.JobDetails
import kotlinx.coroutines.flow.Flow

class JobRepository(private val dao: JobDao) {

    suspend fun create(job: Job) = dao.insert(job)

    fun getJobDetails(): Flow<List<JobDetails>> {
        return dao.getJobDetails()
    }

    suspend fun delete(job: Job) = dao.delete(job)
}