package com.example.pethelper.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "event_table")
data class EventDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Date,
    val type: String,
    @ColumnInfo(name = "pet_Id")
    val petId: Long
)
