package com.example.terrabill.data.local

import androidx.room.*
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.RequestStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Query("SELECT * FROM requests ORDER BY dateTime ASC")
    fun getAllRequests(): Flow<List<Request>>

    @Query("SELECT * FROM requests WHERE status= :status")
    fun getRequestsByStatus(status: RequestStatus): Flow<List<Request>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: Request)

    @Delete
    suspend fun delete(request: Request)

    @Query("UPDATE requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: RequestStatus)
}