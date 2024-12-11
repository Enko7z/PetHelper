package com.example.pethelper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pethelper.entities.EventDbEntity
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity

@Dao
interface PersonDao {
    // PERSON
    @Query("SELECT * FROM person_table WHERE person_login = :login AND person_password = :password")
    fun getPersons(login: String, password: String): List<PersonDbEntity>

    @Insert
    fun insertPerson(person: PersonDbEntity): Long

    // PET

    @Query("SELECT * FROM pet_table WHERE person_id = :personId")
    fun getPets(personId: Long): List<PetDbEntity>

    @Insert
    fun insertPet(pet: PetDbEntity): Long

    @Delete
    fun deletePet(pet: PetDbEntity)

    @Query("DELETE FROM pet_table WHERE name = :petName")
    fun deletePetByName(petName: String)

    // EVENT

    @Query("SELECT * FROM event_table WHERE pet_Id = :petId")
    fun getEvents(petId: Long): List<EventDbEntity>

    @Insert
    fun insertEvent(event: EventDbEntity): Long

    @Delete
    fun deleteEvent(event: EventDbEntity)

//    @Query("SELECT * FROM event_table WHERE pet_Id = :petId AND date >= :cal")
//    fun getEventsForDay(petId: Long, cal: Calendar): List<EventDbEntity>
}