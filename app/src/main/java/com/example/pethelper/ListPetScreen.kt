package com.example.pethelper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pethelper.entities.PetDbEntity
import java.util.Date

class ListPetScreen : AppCompatActivity(), ItemClickListener {
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pet_screen)

        recyclerView = findViewById(R.id.recyclerViewPets)
        recyclerView.layoutManager = LinearLayoutManager(this)

        LoadPets()
    }

    fun LoadPets(){
        var pets: MutableList<PetDbEntity> = mutableListOf()
        viewModel.CallAndWait { pets = viewModel.repository.getPets(loginedPerson.id).toMutableList() }
        var pet = PetDbEntity(0,"+ Добавить","","", Date(), 0)
        pets.add(pet)
        recyclerView.adapter = PetAdapter(pets, this)
    }
    override fun onItemClick(position: Int) {
       val pet: PetDbEntity = (recyclerView.adapter as PetAdapter).pets[position]
        if (pet.id == 0L){
            ShowChangeInfoPetDialog(null, layoutInflater, this,::GoToEventListScreen)
        }else{
            selectedPet = pet
            GoToEventListScreen()
        }
    }

    fun GoToEventListScreen(){
        viewModel.getEvents(selectedPet.id)
        val intent = Intent(this, ActivityMainScreen::class.java)
        startActivity(intent)
    }

    override fun onLongItemClick(position: Int) {
       // код
    }
}