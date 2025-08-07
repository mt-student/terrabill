package com.example.terrabill.data.repository

import com.example.terrabill.data.local.RequestDao
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.RequestStatus
import kotlinx.coroutines.flow.Flow

class RequestRepository(private val dao: RequestDao) {

    fun getRequestsByStatus(status: RequestStatus): Flow<List<Request>> =
        dao.getRequestsByStatus(status)


    suspend fun changeStatus(id: String, status: RequestStatus) =
        dao.updateStatus(id, status)

    suspend fun delete(request: Request) = dao.delete(request)
}