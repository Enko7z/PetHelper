package com.example.pethelper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pethelper.entities.EventDbEntity
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity

@Dao
interface PersonDao {
    // PERSON
    @Query("SELECT * FROM person_table WHERE person_login = :login AND person_password = :password")
    fun getPersons(login: String, password: String): List<PersonDbEntity>

    @Query("SELECT * FROM person_table WHERE person_login = :login LIMIT 1")
    fun findPersonByLogin(login: String): PersonDbEntity?

    @Insert
    fun insertPerson(person: PersonDbEntity): Long

    // PET

    @Query("SELECT * FROM pet_table WHERE person_id = :personId")
    fun getPets(personId: Long): List<PetDbEntity>

    @Insert
    fun insertPet(pet: PetDbEntity): Long

    @Update
    fun updatePet(pet: PetDbEntity)

    @Delete
    fun deletePet(pet: PetDbEntity)

    @Query("DELETE FROM pet_table WHERE name = :petName")
    fun deletePetByName(petName: String)

    @Query("SELECT * FROM pet_table WHERE name = :petName LIMIT 1")
    fun findPetByName(petName: String): PetDbEntity

    // EVENT

    @Query("SELECT * FROM event_table WHERE pet_Id = :petId")
    fun getEvents(petId: Long): List<EventDbEntity>

    @Insert
    fun insertEvent(event: EventDbEntity): Long

    @Delete
    fun deleteEvent(event: EventDbEntity)
}