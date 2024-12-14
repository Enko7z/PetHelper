package com.example.pethelper

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
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
import com.example.pethelper.entities.EventDbEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.Date

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Calendar_frag : Fragment(), ItemClickListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var calendarView: CalendarView
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
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewEvents)

        // Настройка RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Calendar
        calendarView = view.findViewById(R.id.calendar)
        calendarView.setFirstDayOfWeek(CalendarWeekDay.MONDAY) // Установка понедельника, как первый день недели

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                ReloadRecycleView(calendarDay.calendar)
            }
        })

        //Floating action Button
        val floatingActButView: FloatingActionButton = view.findViewById(R.id.floating_action_button)
        floatingActButView.bringToFront()
        floatingActButView.setOnClickListener{
            val cal = calendarView.selectedDates[0]
            ShowAddEventDialog(cal)
        }
        return view
    }
    fun ConnectEventObserver(){
        viewModel.events.observe(this){ eventlist->
            var calendars: ArrayList<CalendarDay> = ArrayList() // массив настроек дней календарных событий
            if (!eventlist.isNullOrEmpty()){
                for (event in eventlist)
                {
                    var calendar = Calendar.getInstance()
                    calendar.time = event.date
                    val calendarDay = CalendarDay(calendar)
                    calendarDay.labelColor = R.color.orange
                    calendarDay.imageResource = R.drawable.ic_event
                    calendars.add(calendarDay)
                }
            }
            calendarView.setCalendarDays(calendars)

            ReloadRecycleView(calendarView.selectedDates[0])
        }
    }

    fun ReloadRecycleView(calendar: Calendar){
        val selEventList: MutableList<EventDbEntity>  // список событий текущего дня
        val eventlist: List<EventDbEntity>? = viewModel.events.value
        if (!eventlist.isNullOrEmpty()){
            selEventList = eventlist.filter { e ->
                e.date.time == calendar.timeInMillis
            }.sortedBy { it.date }.toMutableList()
        }else{
            selEventList = mutableListOf() // список событий текущего дня пустой
        }
        recyclerView.adapter = EventAdapter(selEventList, this)
    }

    override fun onItemClick(position: Int) {
        // код
    }

    override fun onLongItemClick(position: Int) {
        ShowRemoveEventDialog(requireContext(),(recyclerView.adapter as EventAdapter).events[position])
    }

    fun ShowRemoveEventDialog(context: Context, event: EventDbEntity) {
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Удалить событие")
        alertDialogBuilder.setMessage("Вы действительно хотите удалить событие \"${event.name}?\"")
        alertDialogBuilder.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
            // удаляем
            viewModel.CallAndWait { viewModel.repository.deleteEvent(event) }
            // обновляем события
            viewModel.getEvents(selectedPet.id)
            Toast.makeText(requireContext(), "Событие \"${event.name}?\" удалено", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("Отмена") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        var alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun ShowAddEventDialog(cal: Calendar) {
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
        selectDateButton.text = DateToString(cal.time)

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
                val eventDate = Date(y - 1900, m, d)
                val eventType = eventTypeSpinner.selectedItem.toString()
                // добавление ивента и отображение, обновить список ивентов
                val event = EventDbEntity(0, eventName, eventDate, eventType, selectedPet.id)
                viewModel.CallAndWait({ viewModel.repository.insertEvent(event)})
                viewModel.getEvents(selectedPet.id)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                Calendar_frag().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
