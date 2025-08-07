package com.example.terrabill.data.local

import androidx.room.TypeConverter
import com.example.terrabill.data.model.RequestStatus

class Converters {

    @TypeConverter
    fun fromRequestStatus(value: RequestStatus): String = value.name

    @TypeConverter
    fun toRequestStatus(value: String): RequestStatus = RequestStatus.valueOf(value)
}