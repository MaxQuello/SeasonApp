package com.example.seasonapp

import AdapterOfferte
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.BottomsheetlayoutBinding
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.model.RequestPrenotaCamera
import com.example.seasonapp.model.RequestRoom
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate



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
    private var selectedOffer : ArrayList<Offerta>? = null
    var idUtente : Int? = null
    private lateinit var dbManager: DbManager
    private lateinit var bottomLayout: Dialog
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentPrenotaBinding.inflate(layoutInflater)

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()
        mainActivity = activity as MainActivity

        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

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
                    if (checkiflogindone()){
                        searchRoom()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Effettua il Login per verificare la disponibilità delle camere.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                    binding.buttonPrenotaOra.setOnClickListener {
                            mainActivity.getDialog().dismiss()
                        findNavController().navigate(R.id.action_global_pagamentoFragment)
                        // prenotaCamera()
                    }



        return binding.root

        }

    private fun prenotaCamera() {
        var roomIdOfferta : Int? = null
        var dataCheckInOfferta : LocalDate? = null
        var dataCheckOutOfferta : LocalDate? = null

        if (selectedOffer != null){
            for (offerte in selectedOffer!!){
                roomIdOfferta=offerte.roomId
                dataCheckInOfferta=offerte.dataCheckIn
                dataCheckOutOfferta= offerte.dataCheckOut
            }
            val requestPrenotaCamera = roomIdOfferta?.let {
                dataCheckInOfferta?.let { it1 ->
                    dataCheckOutOfferta?.let { it2 ->
                        idUtente?.let { it3 ->
                            RequestPrenotaCamera(
                                it,
                                it1,
                                it2,
                                it3
                            )
                        }
                    }
                }
            }
            inserisciCamera(requestPrenotaCamera)
        }else{
            Toast.makeText(
                requireContext(),
                "Devi prima ricercare una camera per prenotarla",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun inserisciCamera(requestPrenotaCamera: RequestPrenotaCamera?) {
        val query = "INSERT INTO reservations (roomId, checkInDate, checkOutDate, ref_reservations) " +
                "VALUES (${requestPrenotaCamera?.roomId}, '${requestPrenotaCamera?.dataCheckIn}', " +
                "'${requestPrenotaCamera?.dataCheckOut}', $idUtente);"


        ClientNetwork.retrofit.addReservationRoom(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Prenotazione effettuata",
                            Toast.LENGTH_SHORT
                        ).show()
                        val dateCheckInString = requestPrenotaCamera?.dataCheckIn.toString()
                        val dateCheckOutString = requestPrenotaCamera?.dataCheckOut.toString()
                        requestPrenotaCamera?.roomId?.let {
                            idUtente?.let { it1 ->
                                dbManager.insertPrenotazioneCamera(
                                    it,dateCheckInString,
                                    dateCheckOutString, it1
                                )
                            }
                        }
                    }
                    else{
                        val errorMessage = response.message()
                        Log.e("onResponse", "Errore nell'inserimento nel database: $errorMessage")
                        Toast.makeText(
                            requireContext(),
                            "La tua richiesta non è andata a buon fine",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Servizi_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
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
            val camereNecessarie = calcolaCamere(numberOfGuests, numberOfRooms)
            val requestRoom = RequestRoom(checkInDate,checkOutDate,numberOfGuests,numberOfRooms)
            if(camereNecessarie != null) {
                ricercaCamereDB(checkInDate, checkOutDate, camereNecessarie)
            }else{
                //gestisci richiesta impossibile
            }
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

    fun calcolaCamere(nOspiti: Int, nCamere:Int): HashMap<Int, Int>?{
        val camereIndividuate = HashMap<Int, Int>()
        camereIndividuate[1] = 0
        camereIndividuate[2] = 0
        camereIndividuate[4] = 0
        val dimensioniCamere = arrayOf(4, 2, 1)
        var camereRimanenti = nCamere
        if(nCamere <= nOspiti){
            var ospitiSenzaCamera = nOspiti
            while(ospitiSenzaCamera > 0){

                val n = ospitiSenzaCamera.toDouble() / camereRimanenti
                var dimensioneCameraPiuGrande = kotlin.math.ceil(n).toInt()

                if(dimensioneCameraPiuGrande > 4){
                    return null
                }

                while(dimensioneCameraPiuGrande !in dimensioniCamere){
                    dimensioneCameraPiuGrande++
                }

                ospitiSenzaCamera -= dimensioneCameraPiuGrande
                camereRimanenti--

                val numeroCamereNuovo = camereIndividuate[dimensioneCameraPiuGrande]?.plus(1)
                if(numeroCamereNuovo != null) {
                    camereIndividuate[dimensioneCameraPiuGrande] = numeroCamereNuovo
                }

                println(dimensioneCameraPiuGrande)

            }
            return camereIndividuate
        }else{
            return null
        }
    }

    private fun ricercaCamereDB(checkInDate: LocalDate, checkOutDate: LocalDate, camereNecessarie: Map<Int, Int>) {
        val query = "(SELECT * FROM rooms WHERE capacity = 1 " +
                "AND availability = TRUE AND roomId NOT IN (" +
                    "SELECT roomId FROM reservations WHERE (checkInDate <= '${checkInDate}' " +
                    "AND checkOutDate >= '${checkOutDate}')) " +
                "LIMIT ${camereNecessarie[1]}) " +
                "UNION (SELECT * FROM rooms WHERE capacity = 2 " +
                "AND availability = TRUE AND roomId NOT IN (" +
                    "SELECT roomId FROM reservations WHERE (checkInDate <= '${checkInDate}' " +
                    "AND checkOutDate >= '${checkOutDate}')) " +
                "LIMIT ${camereNecessarie[2]}) " +
                "UNION (SELECT * FROM rooms WHERE capacity = 4 " +
                "AND availability = TRUE AND roomId NOT IN (" +
                    "SELECT roomId FROM reservations WHERE (checkInDate <= '${checkInDate}' " +
                    "AND checkOutDate >= '${checkOutDate}')) " +
                " LIMIT ${camereNecessarie[4]})"
        Log.i("MyTag", query)

        ClientNetwork.retrofit.getAvaibleRooms(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {
                        binding.buttonPrenotaOra.visibility = View.VISIBLE
                        val listaOfferte = ArrayList<Offerta>()
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        Log.d("QUERY","risultati:  ${jsonArray}")
                        val resultList = jsonArray?.mapNotNull { it as? JsonObject }
                        Log.d("listarisultati","lista risultati:  ${resultList}")
                        if (resultList != null) {
                            for(jsonObject in resultList){
                                val capacita = jsonObject["capacity"].toString().toInt()
                                val tipologia = jsonObject["roomType"].toString().replace("\"", "")
                                val roomId = jsonObject["roomId"].toString().toInt()
                                val costo = jsonObject["costo"].toString().toDouble()

                                listaOfferte.add(Offerta(roomId,tipologia, 1, costo, checkInDate, checkOutDate, capacita))
                                selectedOffer = listaOfferte
                            }

                        }
                        binding.listaOfferte.adapter = AdapterOfferte(listaOfferte)
                        binding.listaOfferte.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    }

                    else {
                        binding.buttonPrenotaOra.visibility = View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }

}
