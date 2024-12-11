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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainPage_frag.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainPage_frag : Fragment(), ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        InitEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewEvents)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //UpdateEventsForRecycleView(recyclerView, Calendar.getInstance(), false, this@MainPage_frag)
        return view

    }
    fun InitEvents(){
        // вызов GetEvents()
        viewModel.getEvents(selectedPet.id)
        // observe
        viewModel.events.observe(this){ eventlist->
            val DayInCalendar: Calendar = Calendar.getInstance()
            val SelEventList: MutableList<EventDbEntity>
            if (!eventlist.isNullOrEmpty()){
                SelEventList = eventlist.filter { e ->
                    e.date.time >= DayInCalendar.timeInMillis
                }.sortedBy { it.date }.toMutableList()
            }
            else{
                // пустой список
                SelEventList = mutableListOf()
            }
            recyclerView.adapter = EventAdapter(SelEventList, this@MainPage_frag)
        }
    }

    override fun onItemClick(position: Int) {
//         // Handle item click
//        Toast.makeText(requireContext(), "Item clicked at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onLongItemClick(position: Int) {
//        // Handle long item click
//        val event =  (recyclerView.adapter as EventAdapter).events[position]
//        //createDialogForRemove(requireContext(), event.Name)
//        val nn: String = event.Name
//        Toast.makeText(requireContext(), "Item long clicked at position $nn & $position ", Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Menu.
         */
        // TODO: Rename and change types and number of parameters
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