package com.example.pethelper

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Calendar_frag.newInstance] factory method to
 * create an instance of this fragment.
 */
class Calendar_frag : Fragment(), ItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var calendarView: CalendarView
    var calendars: ArrayList<CalendarDay> = ArrayList() // массив настроек дней календарных событий
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewEvents)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Calendar
        calendarView = view.findViewById(R.id.calendar)
        calendarView.setFirstDayOfWeek(CalendarWeekDay.MONDAY) // Установка понедельника, как первый день недели

        var pet = person.Pets[0]
        for (event in pet.Events) {
            AddEventInCalendar(calendars, event)
        }

        calendarView.setCalendarDays(calendars)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                //UpdateEventsForDayInRecycleView(calendarDay.calendar)
                //UpdateEventsForRecycleView(recyclerView, calendarDay.calendar, true, this@Calendar_frag)
            }
        })

        //Floating action Button
        val floatingActButView: FloatingActionButton = view.findViewById(R.id.floating_action_button)
        floatingActButView.bringToFront()
        floatingActButView.setOnClickListener{
            val cal = calendarView.selectedDates[0]
            showEventDialog(cal)
        }

        //UpdateEventsForRecycleView(recyclerView, calendarView.selectedDates[0], true, this@Calendar_frag)
        return view
    }
    override fun onItemClick(position: Int) {
//        // Handle item click
//        Toast.makeText(requireContext(), "Item clicked at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onLongItemClick(position: Int) {
        // Handle long item click
        Toast.makeText(requireContext(), "Item long clicked at position $position", Toast.LENGTH_SHORT).show()
    }

    fun AddEventInCalendar(calendars: ArrayList<CalendarDay>, event: Event){

        var calendar = Calendar.getInstance()

        var date = event.Date
        calendar.time = date
        val calendarDay = CalendarDay(calendar) //
        calendarDay.labelColor = R.color.orange
        calendarDay.imageResource = R.drawable.ic_event
        calendars.add(calendarDay)
    }

    private fun showEventDialog(cal: Calendar) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_event, null)
        val eventNameEditText = dialogView.findViewById<EditText>(R.id.eventNameEditText)
        val selectDateButton = dialogView.findViewById<Button>(R.id.selectDateButton)
        val eventTypeSpinner = dialogView.findViewById<Spinner>(R.id.eventTypeSpinner)

        // Настройка Spinner
        val eventTypes = resources.getStringArray(R.array.eventType)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventTypeSpinner.adapter = spinnerAdapter

        // Устанавливаем значение "Остальное" по умолчанию
        val defaultPosition = eventTypes.indexOf("Остальное")
        if (defaultPosition >= 0) {
            eventTypeSpinner.setSelection(defaultPosition)
        }

        var (d, m, y) = getDateValues(cal)
        selectDateButton.text = "$d-${m + 1}-$y"

        // Настройка кнопки выбора даты
        selectDateButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    d = dayOfMonth
                    m = month
                    y = year
                    selectDateButton.text = "$d-${m + 1}-$y"
                },
                y, m, d
            )
            datePicker.show()
        }

        // Создание AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Создать событие")
            .setView(dialogView)
            .setPositiveButton("OK", null) // Устанавливаем слушатель позже
            .setNegativeButton("Отмена", null)
            .create()

        // Включаем логику обновления состояния кнопки OK
        dialog.setOnShowListener {
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.isEnabled = false // Сначала отключаем кнопку

            // Функция проверки заполненности
            val validateInputs = {
                okButton.isEnabled = eventNameEditText.text.isNotBlank()
            }

            // Слушатель для EditText
            eventNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    validateInputs()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            // Действие при подтверждении
            okButton.setOnClickListener {
                val eventName = eventNameEditText.text.toString()
                val eventType = eventTypeSpinner.selectedItem.toString()
                val eventDate = Date(y - 1900, m, d)
                // добавление ивента и отображение, обновить список ивентов
                val event = Event(eventName, eventDate, eventType)
                person.Pets[0].Events.add(event)
                AddEventInCalendar(calendars, event)
                //UpdateEventsForRecycleView(recyclerView, cal, true, this@Calendar_frag)
                calendarView.setCalendarDays(calendars)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Calendar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                Calendar_frag().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
