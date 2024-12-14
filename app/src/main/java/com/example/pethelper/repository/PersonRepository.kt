package com.example.pethelper.repository

import android.content.Context
import androidx.room.Room
import com.example.pethelper.database.AppDatabase
import com.example.pethelper.entities.EventDbEntity
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity

interface PersonRepository {
    // person
    fun getPersons(login: String, password: String): List<PersonDbEntity>
    fun findPersonByLogin(login: String): PersonDbEntity?
    fun insertPerson(person: PersonDbEntity): Long
    // pet
    fun getPets(personId: Long): List<PetDbEntity>
    fun insertPet(pet: PetDbEntity): Long
    fun updatePet(pet: PetDbEntity)
    fun deletePet(pet: PetDbEntity)
    fun findPetByName(petName: String): PetDbEntity

    suspend fun deletePetByName(petName: String)
    // event
    fun getEvents(petId: Long): List<EventDbEntity>
    fun insertEvent(event: EventDbEntity): Long
    fun deleteEvent(event: EventDbEntity)

    class Base(context: Context) : PersonRepository {
        private val room = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            //.createFromAsset("database/test.db")
            .build()

        private val dao = room.getPersonDao()

        // person
        override fun getPersons(login: String, password: String) = dao.getPersons(login, password)
        override fun findPersonByLogin(login: String) = dao.findPersonByLogin(login)
        override fun insertPerson(person: PersonDbEntity) = dao.insertPerson(person)

        // pet
        override  fun getPets(personId: Long) = dao.getPets(personId)
        override fun insertPet(pet: PetDbEntity) = dao.insertPet(pet)
        override fun updatePet(pet: PetDbEntity) = dao.updatePet(pet)
        override fun deletePet(pet: PetDbEntity) = dao.deletePet(pet)
        override suspend fun deletePetByName(petName: String) = dao.deletePetByName(petName)
        override fun findPetByName(petName: String) = dao.findPetByName(petName)

        // event
        override fun getEvents(petId: Long) = dao.getEvents(petId)
        override fun insertEvent(event: EventDbEntity) = dao.insertEvent(event)
        override fun deleteEvent(event: EventDbEntity) = dao.deleteEvent(event)

        companion object {
            private const val DATABASE_NAME = "test"
        }
    }
}