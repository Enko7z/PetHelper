package com.example.pethelper.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "pet_table")
data class PetDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val breed: String,
    val birthDate: Date,
    @ColumnInfo(name = "person_id")
    val personId: Long
)
