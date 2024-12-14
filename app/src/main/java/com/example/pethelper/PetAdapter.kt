package com.example.pethelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pethelper.entities.PetDbEntity

class PetAdapter(val pets: MutableList<PetDbEntity>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    // ViewHolder для управления отдельными элементами
    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val petName: TextView = itemView.findViewById(R.id.tvPetName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(adapterPosition)
        }

        fun bind(pet: PetDbEntity) {
            petName.text = pet.name
        }
    }

    // Создание нового ViewHolder с макетом
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return PetViewHolder(view)
    }

    // Привязка данных к ViewHolder для конкретной позиции в списке
    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet)
    }

    // Возвращаем общее количество элементов в списке
    override fun getItemCount(): Int = pets.size
}