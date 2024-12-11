package com.example.pethelper.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pethelper.entities.EventDbEntity

@Dao
interface EventDao {

    @Insert
    suspend fun insertEvent(event: EventDbEntity)

    @Update
    suspend fun updateEvent(event: EventDbEntity)

    @Query("SELECT * FROM event_table WHERE id = :id")
    suspend fun getEventById(id: Long): EventDbEntity?

    @Query("DELETE FROM event_table WHERE id = :id")
    suspend fun deleteEventById(id: Long)

    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<EventDbEntity>
}