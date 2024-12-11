package com.example.pethelper.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_table")
data class PersonDbEntity(
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "person_id")
    val id: Long = 0,

    @ColumnInfo(name = "person_login")
    val login: String,

    @ColumnInfo(name = "person_password")
    val password: String
)
