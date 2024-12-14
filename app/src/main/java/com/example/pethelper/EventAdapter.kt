package com.example.pethelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pethelper.entities.EventDbEntity

interface ItemClickListener {
    fun onItemClick(position: Int)
    fun onLongItemClick(position: Int)
}

class EventAdapter(val events: MutableList<EventDbEntity>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder для управления отдельными элементами
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        val eventName: TextView = itemView.findViewById(R.id.tvEventName)
        val eventDate: TextView = itemView.findViewById(R.id.tvEventDate)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            itemClickListener.onLongItemClick(adapterPosition)
            return true
        }

        fun bind(event: EventDbEntity) {
            eventName.text = event.name
            eventDate.text = DateToString(event.date)
        }
    }

    // Создание нового ViewHolder, который использует макет event_item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    // Привязка данных к ViewHolder для конкретной позиции в списке
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    // Возвращаем общее количество элементов в списке
    override fun getItemCount(): Int = events.size
}
