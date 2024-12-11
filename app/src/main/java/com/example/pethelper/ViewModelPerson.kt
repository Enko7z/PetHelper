package com.example.pethelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.entities.EventDbEntity
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity
import com.example.pethelper.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelPerson(val repository: PersonRepository) : ViewModel() {
    val persons = MutableLiveData<List<PersonDbEntity>>()
    val pets = MutableLiveData<List<PetDbEntity>>()
    val events = MutableLiveData<List<EventDbEntity>>()

    // PERSON
    fun getPersons(login: String, password: String) {
        viewModelScope.launch {
            persons.value = withContext(Dispatchers.IO) {
                repository.getPersons(login, password)
            }
        }
    }

    fun insertPerson(person: PersonDbEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertPerson(person)
            }
        }
    }

    // PET
    fun getPets(id: Long) {
        viewModelScope.launch {
            pets.value = withContext(Dispatchers.IO) {
                repository.getPets(id)
            }
        }
    }

    fun insertPet(pet: PetDbEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertPet(pet)
            }
        }
    }

    fun deletePet(pet: PetDbEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deletePet(pet)
            }
        }
    }

    fun deletePetByName(petName: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deletePetByName(petName)
            }
        }
    }

    // EVENT
    fun getEvents(petId: Long){
        viewModelScope.launch {
            events.value = withContext(Dispatchers.IO) {
                repository.getEvents(petId)
            }
        }
    }
    fun insertEvent(event: EventDbEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertEvent(event)
            }
        }
    }
    fun deleteEvent(event: EventDbEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteEvent(event)
            }
        }
    }
//    fun getEventsForDay(petId: Long, cal: Calendar){
//        viewModelScope.launch {
//            events.value = withContext(Dispatchers.IO) {
//                repository.getEventsForDay(petId, cal)
//            }
//        }
//    }
}