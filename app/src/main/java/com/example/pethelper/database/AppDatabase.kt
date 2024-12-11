package com.example.pethelper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pethelper.DateConverter
import com.example.pethelper.dao.PersonDao
import com.example.pethelper.entities.EventDbEntity
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity

@Database(
    version = 6,
    entities = [PersonDbEntity::class, PetDbEntity::class, EventDbEntity::class]
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPersonDao(): PersonDao

}