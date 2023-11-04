package com.overdevx.myclock.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: AlarmEntity)

    @Delete
    suspend fun delete(alarm: AlarmEntity)

    @Update
    suspend fun update(alarm: AlarmEntity)
    @Query("select * from alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>>
}