package com.example.pethelper

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvEventName: TextView = itemView.findViewById(R.id.tvEventName)
    val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
//    val cbEventCompleted: CheckBox = itemView.findViewById(R.id.cbEventCompleted)

    fun bind(event: Event) {
        tvEventName.text = event.Name
        tvEventDate.text = event.Date.toString() // Форматируйте дату, если нужно
//        cbEventCompleted.isChecked = event.Completed
    }
}
