package com.example.seasonapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.databinding.FragmentServiziBinding
import com.example.seasonapp.model.RequestGym
import com.example.seasonapp.model.RequestImpianti
import com.example.seasonapp.model.RequestResturant
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class ServiziFragment : Fragment() {
    private lateinit var binding: FragmentServiziBinding
    private lateinit var datePickerButton : Button
    private lateinit var guestButton : Button
    private lateinit var prenotaGymButton: Button
    private var selectedDate: LocalDate? = null
    private var selectedGuests = 1

    private lateinit var datePickerSportButton: Button
    private lateinit var prenotaOraSportButton: Button
    private var selectedDateImpianti: LocalDate? = null

    var idUtente : Int? = null
    private lateinit var dbManager: DbManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View {
        binding = FragmentServiziBinding.inflate(layoutInflater)

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()


        Log.d("USERNAME","${username}")

        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        datePickerButton = binding.datePickerPalestra
        datePickerButton.setOnClickListener {
            showDatePicker()
            Log.d("Prova", "La data selezionata è: ${selectedDate}")
        }

        guestButton = binding.ospitiPickerPalestra
        guestButton.setOnClickListener {
            showGuestsSelectionDialog()
            Log.d("Prova","Il numero di ospiti selezionati è : ${selectedGuests}")
        }



        prenotaGymButton = binding.buttonPrenotaOraPalestra
        prenotaGymButton.setOnClickListener {
            if(!checkiflogindone()){
                Toast.makeText(
                    requireContext(),
                    "Devi fare l'accesso per prenotare",
                    Toast.LENGTH_LONG).show()
            }
            lifecycleScope.launch {
                if (!checkIfReservationExists()) {
                    Toast.makeText(
                        requireContext(),
                        "Devi prima prenotare una stanza per accedere al ristorante",
                        Toast.LENGTH_LONG
                    ).show()
                }

                if (checkiflogindone() && checkIfReservationExists()) {
                    prenotaPalestra()
                }
            }


        }

        datePickerSportButton = binding.datePickerImpianto
        datePickerSportButton.setOnClickListener {
            showDatePickerImpianti()
        }

        prenotaOraSportButton = binding.buttonPrenotaOraImpianto
        prenotaOraSportButton.setOnClickListener {
            prenotaImpianto()
        }

        return binding.root
    }

    private fun showDatePicker() {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        val currentDay = currentDate.dayOfMonth

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                this@ServiziFragment.selectedDate = LocalDate.of(year, month + 1, day)

                if (selectedDate?.isBefore(currentDate) == true) {
                    // La data selezionata è precedente alla data corrente
                    // Mostra un messaggio di errore o prendi un'altra azione appropriata
                    Toast.makeText(
                        requireContext(),
                        "La data di prenotazione non può essere precedente alla data corrente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Imposta la data di prenotazione selezionata
                    // Esegui le operazioni desiderate con la data selezionata
                    updateButtonWithSelectedDate()
                }
            },
            currentYear,
            currentMonth - 1,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun updateButtonWithSelectedDate() {
        val dateString = selectedDate?.toString() ?: ""
        datePickerButton.text = dateString
    }

    private fun showDatePickerImpianti() {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        val currentDay = currentDate.dayOfMonth

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                this@ServiziFragment.selectedDateImpianti = LocalDate.of(year, month + 1, day)

                if (selectedDateImpianti?.isBefore(currentDate) == true) {
                    Toast.makeText(
                        requireContext(),
                        "La data di prenotazione non può essere precedente alla data corrente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    updateButtonWithSelectedDateImpianti()
                }
            },
            currentYear,
            currentMonth - 1,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun updateButtonWithSelectedDateImpianti() {
        val dateString = selectedDateImpianti?.toString() ?: ""
        datePickerSportButton.text = dateString
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
        val ospitiPickerButton: Button = binding.ospitiPickerPalestra
        ospitiPickerButton.text = "Numero di ospiti: $selectedGuests"
    }

    private fun prenotaPalestra() {
        val numberOfGuest = selectedGuests
        val gymDate = selectedDate
        val idRicevuto = idUtente

        if (numberOfGuest > 0 && gymDate != null) {
            val requestGym = idRicevuto?.let { RequestGym(it, gymDate, numberOfGuest) }
            requestGym?.let { prenotazionePalestra(it) }
        } else {
            Toast.makeText(
                requireContext(),
                "Non hai completato i campi richiesti",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun prenotazionePalestra(requestGym: RequestGym) {
        val query = "INSERT INTO prenotazioneGym(id_utente, data_prenotazione, numero_ospiti)" +
                " VALUES(${requestGym.idUtente}, '${requestGym.gymDate}'," + " ${requestGym.numeroOspiti});"

        Log.d("QUERY","La mia query è: ${query}")

        ClientNetwork.retrofit.insertGymReservation(query).enqueue(
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
                        val dateString = requestGym.gymDate.toString()
                        dbManager.insertPrenotazioneGym(requestGym.idUtente,dateString,requestGym.numeroOspiti)


                    }else{
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

    private suspend fun checkIfReservationExists(): Boolean {
        val query = "SELECT * FROM reservations WHERE ref_reservations = $idUtente " +
                "AND '$selectedDate' BETWEEN checkInDate AND checkOutDate"

        Log.d("QUERY","LA QUERY E': ${query}")
        var reservationExists = false

        try {
            val response = withContext(Dispatchers.IO) {
                ClientNetwork.retrofit.getMyReservations(query).execute()
            }

            if (response.isSuccessful) {
                val reservations = response.body()
                Log.d("RESERVATIONS BODY","BODY: $reservations")
                reservationExists = (reservations?.getAsJsonArray("queryset")?.size() ?: 0) > 0

                Log.d("RESERVATIONS BODY","BODY: $reservationExists")
            } else {
                Toast.makeText(requireContext(), "Andata male", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.i("LOG-Prenota_Fragmemt-onFailure", "Errore accesso ${e.message}")
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        Log.d("RETURN","Il return è : $reservationExists")

        return reservationExists
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

    private fun prenotaImpianto(){
        val idRicevuto = idUtente
        val impiantiDate = selectedDateImpianti

        if (impiantiDate != null) {
            val requestImpianti = idRicevuto?.let { RequestImpianti(it, impiantiDate) }
            inserisciPrenotazione(requestImpianti)
        } else {
            Toast.makeText(
                requireContext(),
                "Non hai completato i campi richiesti",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun inserisciPrenotazione(requestImpianti: RequestImpianti?) {
        val query = "INSERT INTO prenotazioneImpianti(ref_impianti, data_prenotazione_impianti)" +
                " VALUES(${requestImpianti?.idUtente}, '${requestImpianti?.impiantiDate}');"

        Log.d("QUERY","QUERY: $query")

        ClientNetwork.retrofit.inserImpiantiReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Toast.makeText(
                            requireContext(),
                        "Prenotazione effettuata",
                        Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "La tua prenotazione non è andata a buon fine",
                            Toast.LENGTH_LONG
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

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }

}