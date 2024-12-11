package com.example.pethelper

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pethelper.databinding.ActivityMainBinding
import com.example.pethelper.entities.PersonDbEntity
import com.example.pethelper.entities.PetDbEntity
import com.example.pethelper.repository.PersonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date

class Event {
    //var Id: UUID
    var Name: String
    var Date: Date
    var Type: String
    //var Completed:Boolean
    constructor(name: String, date: Date, type: String)//, completed: Boolean = false)
    {
        //Id = UUID.randomUUID()
        Name = name
        Date = date
        Type = type
       //Completed = completed
    }
}

class Pet {
    var Name: String
    var Type: String
    var Breed: String
    var Date: Date
    var Events: MutableList<Event>

    constructor(name: String, type: String, breed: String, date: Date)
    {
        Name = name
        Type = type
        Breed = breed
        Date = date
        Events = mutableListOf<Event>()
        //Events.add(Event("gfdgfd", Date("020102002")))
    }
}

class Person {
    var Login: String
    var Password: String
    var Pets: MutableList<Pet>
    constructor(login: String, password: String)
    {
        Login = login
        Password = password
        Pets = mutableListOf<Pet>()
    }
}

lateinit var person: Person
//var pet: Pet? = null

class PersonContainer{
    lateinit var person: Person
}

fun GetPerson(login: String, password: String, pc: PersonContainer) : Boolean{

    //if (login == "user" && password == "1234") {
        pc.person = Person(login, password)
        val pet = Pet("Пуся", "Собака", "Лабрадор", Date())//pet = Pet("Буся")
        pc.person.Pets.add(pet)
        val event = Event("то-от", Date(2024 - 1900,11,19), "gfdgd")
        val event2 = Event("Мишки", Date(2024 - 1900,11,30), "gfdgd")
        pet.Events.add(event)
        pet.Events.add(event2)
        pet.Events.sortBy { event -> event.Date }
        return true
//    } else {
//        return false
//    }
}

fun getEvents(): MutableList<Event> {
    return mutableListOf(
        Event("Meeting", Date(), "Work"),
        Event("Dentist", Date(), "Health"),
        Event("Meeting", Date(), "Work"),
        Event("Dentist", Date(), "Health"),
        Event("Meeting", Date(), "Work"),
        Event("Dentist", Date(), "Health"),
        Event("Meeting", Date(), "Work"),
        Event("Dentist", Date(), "Health"),
        // Добавьте свои события
    )
}

fun getDateValues(calendar: Calendar) : Triple<Int, Int, Int>{
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    return Triple(day, month, year)
}

//fun UpdateEventsForRecycleView(recycleview: RecyclerView, DayInCalendar: Calendar, OnlyThisDay: Boolean, intf: ItemClickListener){
//    recycleview.adapter = EventAdapter(
//        person.Pets[0].Events.filter { e ->
//            if (OnlyThisDay) e.Date.time == DayInCalendar.timeInMillis
//            else e.Date.time >= DayInCalendar.timeInMillis
//        }.sortedBy { it.Date }.toMutableList()
//        , intf
//    )
//}


fun createDialogForRemove(context: Context, eventName: String) {// , eventID: UUID) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setTitle("Удалить событие")
    alertDialogBuilder.setMessage("Вы действительно хотите удалить событие \"$eventName?\"")
    alertDialogBuilder.setPositiveButton("Удалить") { _: DialogInterface, _: Int ->
        // удаляем
        //val eventForRemove = person.Pets[0].Events.find { it.id == eventID }
//        if (eventForRemove != null){
//            person.Pets[0].Events.remove(eventForRemove)
//            Toast.makeText(context, "Событие удалено", Toast.LENGTH_SHORT).show()
//        }
//        else{
//            Toast.makeText(context, "Событие не удалено (не найдено)", Toast.LENGTH_SHORT).show()
//        }
        //обновить recyclerview
    }
    alertDialogBuilder.setNegativeButton("Отмена") { dialogInterface: DialogInterface, i: Int ->
        dialogInterface.dismiss() // закрываем
    }
    var alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}

//lateinit var db: PersonRepository
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
        val nnn: String = "s3"
        Initdb()
        //viewModel.deletePetByName("Test")
//        val newPerson = PersonDbEntity(0,nnn,nnn)
//        viewModel.insertPerson(newPerson)
//
//        viewModel.getPersons(nnn, nnn)
//
//        viewModel.persons.observe(this) { list ->
//            if (list != null) {
//                val person = list?.first() // Assuming you get one matching record
//                if (person != null) {
//                    if (person.login == nnn) {
//                        val log = person.login
//                        val pas = person.password
//                        Toast.makeText(this, "FIRST $log - $pas - ${person.id}", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
////                val intent = Intent(this, ActivityMainScreen::class.java)
////                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.loginButton.setOnClickListener {
            if (binding.username.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                val login = binding.username.text.toString()
                val password = binding.password.text.toString()
                LoginUser(login, password)
                // Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }

//            viewModel.getPersons(login, password)
//
//            viewModel.persons.observe(this) { list ->
//                if (list != null) {
//                    val person = list?.first() // Assuming you get one matching record
//                    if (person != null) {
//                        if (person.login == nnn){
//                            val log = person.login
//                            val pas = person.password
//                            Toast.makeText(this, "$log - $pas - ${person.id}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
////                val intent = Intent(this, ActivityMainScreen::class.java)
////                startActivity(intent)
//                } else {
//                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
        }
    }

    fun Initdb() {
        viewModel = ViewModelPerson(PersonRepository.Base(application))
        //Toast.makeText(this, "initdb!", Toast.LENGTH_SHORT).show()
    }

    lateinit var personlist: List<PersonDbEntity>
    fun GetPersons(login: String, password: String) = runBlocking {
        GlobalScope.launch { // запуск новой корутины с сохранением ссылки на нее в Job
            personlist = viewModel.repository.getPersons(login, password)
        }.join() // ждем завершения вложенной корутины
    }

    fun InsertPersons(person: PersonDbEntity) = runBlocking {
        GlobalScope.launch { // запуск новой корутины с сохранением ссылки на нее в Job
            viewModel.repository.insertPerson(person)
        }.join() // ждем завершения вложенной корутины
    }

    private fun LoginUser(login: String, password: String) {
        GetPersons(login, password)

        if (!personlist.isNullOrEmpty()) {
            val tempPerson = personlist.first()
            if (tempPerson.password == password) {
                loginedPerson = tempPerson
                Toast.makeText(this, "Пользователь найден!", Toast.LENGTH_SHORT).show()
                // Переход к выбору питомца, если есть питомец
                InitPets()
            } else {
                Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Нет пользователя с таким логином!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    lateinit var petlist: List<PetDbEntity>
    fun GetPets() = runBlocking {
        GlobalScope.launch { // запуск новой корутины с сохранением ссылки на нее в Job
            petlist = viewModel.repository.getPets(loginedPerson.id)
        }.join() // ждем завершения вложенной корутины
    }

    fun InsertPet(pet: PetDbEntity) = runBlocking {
        GlobalScope.launch { // запуск новой корутины с сохранением ссылки на нее в Job
            viewModel.repository.insertPet(pet)
        }.join() // ждем завершения вложенной корутины
    }

    fun InitPets() {
        GetPets()
        if (!petlist.isNullOrEmpty()) {
            selectedPet = petlist.first()
            Toast.makeText(this, "Есть питомец ${selectedPet.name}", Toast.LENGTH_SHORT).show()
//            viewModel.insertEvent(
//                EventDbEntity(0, "Прогулка",Date(2024 - 1900, 11, 29),"Выгул",
//                selectedPet.id)
//            )
            val intent = Intent(this, ActivityMainScreen::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Нет питомца, регистрируем", Toast.LENGTH_SHORT).show()
            val newpet = PetDbEntity(
                0, "Test", "Кошка", "Сиамская", Date(2009 - 1900, 11, 19),
                loginedPerson.id
            )
            InsertPet(newpet)
            InitPets()
        }
    }
}
//    private fun ShowCreateProfileDialog(login: String, password: String) {
//        AlertDialog.Builder(this).apply {
//            setTitle("Создать профиль")
//            setMessage("Пользователь с логином $login не найден. Создать нового пользователя?")
//            setPositiveButton("Да") { _, _ ->
//                CreateNewUser(login, password)
//            }
//            setNegativeButton("Нет") { dialog, _ ->
//                dialog.dismiss()
//            }
//            create().show()
//        }
//    }
//    private fun CreateNewUser(login: String, password: String) {
//        val newPerson = PersonDbEntity(0, login, password)
//        viewModel.insertPerson(newPerson)
//
//        viewModel.insertResult.observe(this) { resultId ->
//            if (resultId > 0) {
//                Toast.makeText(this, "Пользователь создан!", Toast.LENGTH_SHORT).show()
//                LoginUser(login, password) // Повторяем логин
//            } else {
//                Toast.makeText(this, "Ошибка создания пользователя!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
    //        viewModel.getPersons(login, password)
//
//        viewModel.persons.observe(this) { persList ->
//            Toast.makeText(this, "observe", Toast.LENGTH_SHORT).show()
//            if (!viewModel.persons.value.isNullOrEmpty()) {
//                val tempPerson = viewModel.persons.value?.first()
//                if (tempPerson!!.password == password) {
//                    loginedPerson = tempPerson // Сохраняем пользователя
//                    Toast.makeText(this, "Пользователь найден!", Toast.LENGTH_SHORT).show()
//                    // ShowPetSelectWindow()      // Переход к выбору питомца
//                } else {
//                    Toast.makeText(this, "Неверный пароль!", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                // Нет пользователя с таким логином
//                Toast.makeText(this, "Нет пользователя с таким логином!", Toast.LENGTH_SHORT).show()
//                //ShowCreateProfileDialog(login, password)
//            }
//            viewModel.persons.removeObservers(this)
//            viewModel.persons.value = null
//        }


