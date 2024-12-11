package com.example.pethelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// Для Spinner
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

// Для даты рождения
import android.app.DatePickerDialog
import android.widget.EditText
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile_frag.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile_frag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    // on below line creating a variable.
    lateinit var dateEdt: EditText
    lateinit var et_petName: EditText
    lateinit var adapterTypePet: ArrayAdapter<CharSequence>
    lateinit var adapterBreedCat: ArrayAdapter<CharSequence>
    lateinit var adapterBreedDog: ArrayAdapter<CharSequence>
    lateinit var spinnerType: Spinner
    lateinit var spinnerBreed: Spinner

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
        // Inflate the layout for this fragment
        val frag = inflater.inflate(R.layout.fragment_profile, container, false)

        et_petName = frag.findViewById(R.id.PetName)
        var curPet = person.Pets[0]
        et_petName.setText(curPet.Name)
        // Найдем Spinner по ID для типа
        val spinnerType: Spinner= frag.findViewById(R.id.PetType)
        // Найдем Spinner по ID для породы
        val spinnerBreed: Spinner= frag.findViewById(R.id.PetBreed)

        //Адаптер для кошек
        // Создаем адаптер для данных
        val adapterBreedCat = ArrayAdapter.createFromResource(
            requireContext(), // Используем контекст фрагмента
            R.array.breedCat, // Ресурс массива строк
            android.R.layout.simple_spinner_item // Макет для элемента списка
        )

        // Установим макет для выпадающего списка
        adapterBreedCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Адаптер для собак
        val adapterBreedDog = ArrayAdapter.createFromResource(
            requireContext(), // Используем контекст фрагмента
            R.array.breedDog, // Ресурс массива строк
            android.R.layout.simple_spinner_item // Макет для элемента списка
        )

        // Установим макет для выпадающего списка
        adapterBreedDog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Для спиннера с типом
        // Создаем адаптер для данных
        val adapterTypePet = ArrayAdapter.createFromResource(
            requireContext(), // Используем контекст фрагмента
            R.array.typePet, // Ресурс массива строк
            android.R.layout.simple_spinner_item // Макет для элемента списка
        )
        // Установим макет для выпадающего списка
        adapterTypePet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Установим адаптер в Spinner
        spinnerType.adapter = adapterTypePet
        var ind: Int = adapterTypePet.getPosition(curPet.Type)
        spinnerType.setSelection(ind)

        // Обработчик выбора элемента в Spinner
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), "Вы выбрали: $selectedItem", Toast.LENGTH_SHORT).show()
                if (position == 0)
                {
                    // Установим адаптер в Spinner
                    spinnerBreed.adapter = adapterBreedCat
                } else {
                    // Установим адаптер в Spinner
                    spinnerBreed.adapter = adapterBreedDog
                }
                ind = (spinnerBreed.adapter as ArrayAdapter<CharSequence>).getPosition(curPet.Breed)
                spinnerBreed.setSelection(ind)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Действия, если ничего не выбрано
            }
        }

        // Для спиннера с породой

        // Обработчик выбора элемента в Spinner
        spinnerBreed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), "Вы выбрали: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Действия, если ничего не выбрано
            }
        }
        // Дата рождения
        // on below line we are initializing our variables.
        dateEdt = frag.findViewById(R.id.PetDate)

        // on below line we are adding
        // click listener for our edit text.
        dateEdt.setOnClickListener {

            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                frag.context,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }
        return frag
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile_frag().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}