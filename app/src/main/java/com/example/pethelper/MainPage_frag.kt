package com.example.pethelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pethelper.entities.EventDbEntity
import java.util.Calendar

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainPage_frag : Fragment(), ItemClickListener {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        ConnectEventObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewEvents)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    fun ConnectEventObserver(){
        viewModel.events.observe(this){ eventlist->
            val DayInCalendar: Calendar = Calendar.getInstance()
            val SelEventList: MutableList<EventDbEntity>
            if (!eventlist.isNullOrEmpty()){
                SelEventList = eventlist.filter { e ->
                    e.date.time >= DayInCalendar.timeInMillis
                }.sortedBy { it.date }.toMutableList()
            }
            else{
                SelEventList = mutableListOf() // пустой список
            }
            recyclerView.adapter = EventAdapter(SelEventList, this)
        }
    }

    override fun onItemClick(position: Int) {
       // код
    }

    override fun onLongItemClick(position: Int) {
        // код
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainPage_frag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}