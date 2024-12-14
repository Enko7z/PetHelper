package com.example.pethelper

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pethelper.databinding.ActivityMainBinding
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity
import com.example.pethelper.repository.PersonRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getDateValues(calendar: Calendar) : Triple<Int, Int, Int>{
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    return Triple(day, month, year)
}

fun DateToString(date: Date): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun CreateSpinnerAdapter(context: Context, strings: Int):  ArrayAdapter<CharSequence>{
    val adapter = ArrayAdapter.createFromResource(
        context, // Используем контекст фрагмента
        strings, // Ресурс массива строк
        android.R.layout.simple_spinner_item // Макет для элемента списка
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    return adapter
}

fun ShowChangeInfoPetDialog(pet: PetDbEntity?, inflater: LayoutInflater, context: Context, okclicklistener: () -> Unit) {
    val dialogView = inflater.inflate(R.layout.dialog_petprofile, null)
    val petNameEditText = dialogView.findViewById<EditText>(R.id.PetName)
    val petTypeSpinner = dialogView.findViewById<Spinner>(R.id.PetType)
    val petBreedSpinner = dialogView.findViewById<Spinner>(R.id.PetBreed)
    val petDateEditText = dialogView.findViewById<EditText>(R.id.PetDate)
    // Adapter
    val adapterTypePet: ArrayAdapter<CharSequence>
    val adapterBreedCat: ArrayAdapter<CharSequence>
    val adapterBreedDog: ArrayAdapter<CharSequence>

    val petNotIsNull: Boolean = (pet != null)
    var date: Date

    // Адаптер для типа
    adapterTypePet = CreateSpinnerAdapter(context, R.array.typePet)
    petTypeSpinner.adapter = adapterTypePet

    // Адаптер для кошек
    adapterBreedCat = CreateSpinnerAdapter(context, R.array.breedCat)
    // Адаптер для собак
    adapterBreedDog = CreateSpinnerAdapter(context, R.array.breedDog)
    petBreedSpinner.adapter = adapterBreedCat

    petTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (position == 0) // кошки
            {
                petBreedSpinner.adapter = adapterBreedCat
            } else { // собаки
                petBreedSpinner.adapter = adapterBreedDog
            }
            if (petNotIsNull){
                val ind = (petBreedSpinner.adapter as ArrayAdapter<CharSequence>).getPosition(pet?.breed)
                petBreedSpinner.setSelection(ind)
            }
        }
        override fun onNothingSelected(parent: AdapterView<*>) {
            // Действия, если ничего не выбрано
        }
    }

    // Если есть питомец
    if (petNotIsNull){
        // name
        petNameEditText.setText(pet?.name)
        //type
        var ind: Int = adapterTypePet.getPosition(pet?.type)
        petTypeSpinner.setSelection(ind)
        // date
        date = pet?.birthDate!!
    }else{
        // Настройка Date
        date = Calendar.getInstance().time
    }

    // Установка даты
    petDateEditText.setText(DateToString(date))

    // Выбор даты
    petDateEditText.setOnClickListener {
        val cal = Calendar.getInstance()
        cal.time = date

        var (d, m, y) = getDateValues(cal)

        // Диалог выбора даты рождения
        val datePickerDialog = DatePickerDialog(
            context,
            { view, year, monthOfYear, dayOfMonth ->
                val dat = DateToString(Date(year - 1900,monthOfYear,dayOfMonth))
                petDateEditText.setText(dat)
                date = Date(year - 1900, monthOfYear, dayOfMonth)
            },
            y, m, d
        )
        datePickerDialog.show()
    }

    // Создание AlertDialog
    val dialog = AlertDialog.Builder(context)
        .setTitle("Данные о питомце")
        .setView(dialogView)
        .setPositiveButton("Применить", null) // Устанавливаем слушатель позже
        .setNegativeButton("Отмена", null)
        .create()

    // Включаем логику обновления состояния кнопки OK
    dialog.setOnShowListener {
        val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        okButton.isEnabled = petNotIsNull // Сначала отключаем кнопку, если новый пет

        // Функция проверки заполненности
        val validateInputs = {
            okButton.isEnabled = petNameEditText.text.isNotBlank()
        }

        // Слушатель для EditText
        petNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Действие при подтверждении
        okButton.setOnClickListener {
            val petName = petNameEditText.text.toString()
            val petType = petTypeSpinner.selectedItem.toString()
            val petBreed = petBreedSpinner.selectedItem.toString()
            val petDate = date
            val petIndex: Long
            if(petNotIsNull)
                petIndex = pet!!.id
            else
                petIndex = 0
            // добавление питомца и отображение, обновить список питомцев
            var pet = PetDbEntity(petIndex, petName, petType, petBreed, petDate, loginedPerson.id)

            if(petNotIsNull){
                viewModel.CallAndWait({ viewModel.repository.updatePet(pet)})
                selectedPet = pet
            }else { // создаём нового питомца
                viewModel.CallAndWait({ viewModel.repository.insertPet(pet) })
                viewModel.CallAndWait({ pet = viewModel.repository.findPetByName(petName) })
                selectedPet = pet
                //переход
            }
            okclicklistener()
            dialog.dismiss()
        }
    }
    dialog.show()
}

lateinit var viewModel: ViewModelPerson
lateinit var loginedPerson: PersonDbEntity
lateinit var selectedPet: PetDbEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivity = this
        Initdb()

        binding.loginButton.setOnClickListener {
            if (binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                val login = binding.username.text.toString()
                val password = binding.password.text.toString()
                LoginUser(login, password)
            }
        }
        binding.regButton.setOnClickListener(){
            if (binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                val login = binding.username.text.toString()
                val password = binding.password.text.toString()
                RegisterUser(login, password)
            }
        }
    }

    fun Initdb() {
        viewModel = ViewModelPerson(PersonRepository.Base(application))
    }

    private fun LoginUser(login: String, password: String) {
        var person: PersonDbEntity? = null
        viewModel.CallAndWait { person = viewModel.repository.findPersonByLogin(login) }
        if (person != null) {
            if (person?.password == password) {
                loginedPerson = person!!
                ShowSelectPetWindow()
            } else {
                Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Нет пользователя с таким логином!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun RegisterUser(login: String, password: String) {
        var person: PersonDbEntity? = null
        viewModel.CallAndWait { person = viewModel.repository.findPersonByLogin(login) }
        if (person != null) {
            Toast.makeText(this, "Пользователь с таким логином уже есть!", Toast.LENGTH_SHORT).show()
        }else
        {
            person = PersonDbEntity(0, login, password)
            viewModel.CallAndWait { viewModel.repository.insertPerson(person!!)}
            viewModel.CallAndWait { person = viewModel.repository.findPersonByLogin(person!!.login)}
            loginedPerson = person!!
            ShowSelectPetWindow()
        }
    }

    fun ShowSelectPetWindow(){
        val intent = Intent(this, ListPetScreen::class.java)
        startActivity(intent)
    }
}

