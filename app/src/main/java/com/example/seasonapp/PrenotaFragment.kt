package com.example.seasonapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.model.RequestRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class Camera(
    val roomId: Int,
    val roomType: String,
    val capacity: Int
)



class PrenotaFragment : Fragment() {
    private lateinit var binding: FragmentPrenotaBinding
    private lateinit var datePickerButton : Button
    private lateinit var searchButton: Button
    private lateinit var camerePickerButton : Button
    private var selectedCheckInDate: LocalDate? = null
    private var selectedCheckOutDate: LocalDate? = null
    private var checkInSelected = false
    private lateinit var guestSelection :Button
    private var selectedGuests = 1
    private var selectedRooms: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

//Pulsante Prenota che appare se nella recycler ci sono elementi
    /*    val adapterOfferte = AdapterOfferte(ArrayList())
        val buttonPrenotaOra = binding.buttonPrenotaOra
        if (adapterOfferte.itemCount> 0) {
            buttonPrenotaOra.visibility = View.VISIBLE
        } else {
            buttonPrenotaOra.visibility = View.GONE                 //cercare di capire perchè crasha
        } */



                binding = FragmentPrenotaBinding.inflate(layoutInflater)

                datePickerButton = binding.datePicker
                datePickerButton.setOnClickListener {
                    checkInSelected = false
                    selectedCheckInDate = null
                    selectedCheckOutDate = null
                    showDatePicker()
                }
                guestSelection = binding.ospitiPicker
                guestSelection.setOnClickListener {
                    showGuestsSelectionDialog()
                }

                camerePickerButton = binding.camerePicker
                camerePickerButton.setOnClickListener {
                    showRoomsSelectionDialog()
                }

                searchButton = binding.search
                searchButton.setOnClickListener {
                    if (checkiflogindone()){  //togliere ! quando il db è linkato bene
                        searchRoom()
                        //preleva dati db e popola arrayList offerte
                        /*val offerte = ArrayList<Offerta>()
                        for(i in 1..3){ //qui sostituire il for con un for migliorato che itera il json
                            offerte.add(Offerta("Bellissime",99, 999.99, date?.get(0), date?.get(1), 99)) // qui dentro andranno gli attributi del risultato della query per popolare la cardview
                        }*/

                        /*binding.listaOfferte.adapter = AdapterOfferte(offerte)
                        binding.listaOfferte.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)*/

                    }else{
                        //Mi porta al profilo
                    }
                }

                return binding.root
        }

    private fun showRoomsSelectionDialog() {
        val roomNumberPicker = NumberPicker(context)

        // Imposta le configurazioni del NumberPicker
        roomNumberPicker.minValue = 1 // Numero minimo di camere
        roomNumberPicker.maxValue = 6 // Numero massimo di camere
        roomNumberPicker.value = selectedRooms // Imposta il valore iniziale del NumberPicker

        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Seleziona il numero di camere")
            .setView(roomNumberPicker)
            .setPositiveButton("OK") { dialog, _ ->
                selectedRooms = roomNumberPicker.value
                updateRoomPickerButtonText()
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun updateRoomPickerButtonText() {
        val buttonText = "Numero di camere: $selectedRooms"
        camerePickerButton.text = buttonText
    }


    private fun searchRoom() {
        val numberOfGuests = selectedGuests
        val checkInDate = selectedCheckInDate
        val checkOutDate = selectedCheckOutDate
        val numberOfRooms = selectedRooms
        Log.i("MyTag", checkInDate.toString())

        // Verifica se le informazioni sono valide
        if (numberOfGuests > 0 && checkInDate != null && checkOutDate != null) {
            val requestRoom = RequestRoom(checkInDate,checkOutDate,numberOfGuests,numberOfRooms)
            ricercaCamereDB(requestRoom)
            //return listOf(checkInDate, checkOutDate)
        } else {
            Toast.makeText(
                requireContext(),
                "Completa tutte le informazioni di prenotazione",
                Toast.LENGTH_SHORT
            ).show()
            //return null
        }
    }

    private fun ricercaCamereDB(requestRoom: RequestRoom) {
        val query = "SELECT * FROM rooms " +
                "WHERE (capacity = 1 AND ${requestRoom.numberOfGuest} = 1) " +
                "OR (capacity = 2 AND ${requestRoom.numberOfGuest} <= 2) " +
                "OR (capacity = 4 AND ${requestRoom.numberOfGuest} <= 4) " +
                "AND availability = TRUE " +
                "AND roomId NOT IN " +
                "(SELECT roomId FROM reservations " +
                "WHERE (checkInDate <= '${requestRoom.checkInDate}' AND checkOutDate >= '${requestRoom.checkOutDate}') " +
                "OR (checkInDate <= '${requestRoom.checkInDate}' AND checkOutDate >= '${requestRoom.checkOutDate}')) " +
                "LIMIT ${requestRoom.numberOfRoom}"

        ClientNetwork.retrofit.getAvaibleRooms(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {
                        //Scrivi qui gio
                    }

                    else {
                        Log.e("Errore API", "Codice di errore: ${response.code()}")
                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Prenota_Fragmemt-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )


    }


    private fun checkiflogindone(): Boolean {
        // Ottenere un'istanza delle SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Recuperare lo stato del login
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Verificare lo stato del login
        if (isLoggedIn) {
            Log.i("PROVA","LOGIN FATTO")
            return true
        } else {
            Log.i("PROVA","LOGIN NON FATTO")
            return false
        }

    }

    private fun showDatePicker() {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        val currentDay = currentDate.dayOfMonth

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = LocalDate.of(year, month + 1, day)

                if (selectedDate.isBefore(currentDate)) {
                    // La data di check-in è precedente alla data corrente
                    // Mostra un messaggio di errore o prendi un'altra azione appropriata
                    Toast.makeText(
                        requireContext(),
                        "La data di Check-in non può essere precedente alla data corrente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (!checkInSelected) {
                        selectedCheckInDate = selectedDate
                        checkInSelected = true
                        showDatePicker()
                    } else {
                        if (selectedDate.isAfter(selectedCheckInDate)) {
                            selectedCheckOutDate = selectedDate
                            updateButtonWithSelectedDates()
                        } else {
                            // La data di check-out è precedente o uguale alla data di check-in
                            // Mostra un messaggio di errore o prendi un'altra azione appropriata
                            Toast.makeText(
                                requireContext(),
                                "La data di Check-out deve essere successiva alla data di Check-in",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            currentYear,
            currentMonth - 1,
            currentDay
        )

        datePickerDialog.show()
    }


    private fun updateButtonWithSelectedDates() {
        val checkInDateString = selectedCheckInDate?.toString() ?: ""
        val checkOutDateString = selectedCheckOutDate?.toString() ?: ""

        val buttonText = "$checkInDateString - $checkOutDateString"
        datePickerButton.text = buttonText
    }

    private fun showGuestsSelectionDialog() {
        val guestsNumberPicker = NumberPicker(context)

        // Imposta le configurazioni del NumberPicker
        guestsNumberPicker.minValue = 1 // Numero minimo di ospiti
        guestsNumberPicker.maxValue = 10 // Numero massimo di ospiti
        guestsNumberPicker.value = selectedGuests // Imposta il valore iniziale del NumberPicker

        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Seleziona il numero di ospiti")
            .setView(guestsNumberPicker)
            .setPositiveButton("OK") { dialog, _ ->
                selectedGuests = guestsNumberPicker.value
                updateOspitiPickerButtonText()
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun updateOspitiPickerButtonText() {
        val ospitiPickerButton: Button = binding.ospitiPicker
        ospitiPickerButton.text = "Numero di ospiti: $selectedGuests"
    }

    fun getRooms(jsonObject: JsonObject){
        val roomId = jsonObject.get("roomId").asInt
        val roomType = jsonObject.get("roomType").asString
        val capacity = jsonObject.get("capacity").asInt
    }
}
