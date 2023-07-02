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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentRistoranteBinding
import com.example.seasonapp.model.RequestResturant
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class RistoranteFragment : Fragment() {
    private lateinit var binding: FragmentRistoranteBinding
    private lateinit var datePickerButton: Button
    private lateinit var guestButton: Button
    private lateinit var prenotaButton: Button
    private lateinit var radioGroupPasto: RadioGroup
    private lateinit var chosenMeal : String
    private var selectedDate: LocalDate? = null
    private var selectedGuests = 1
    private lateinit var dbManager: DbManager
    private var idUtente : Int? = null
    private var typeNotification = "Ristorante"
    private var textNotification : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRistoranteBinding.inflate(layoutInflater)

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()


        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        datePickerButton = binding.datePickerRistorante
        datePickerButton.setOnClickListener {
            showDatePicker()
        }



        guestButton = binding.ospitiPickerRistorante
        guestButton.setOnClickListener {
            showGuestsSelectionDialog()
        }

        radioGroupPasto = binding.groupPasto


        prenotaButton = binding.buttonPrenotaOraRistorante
        prenotaButton.setOnClickListener {
            val radioButtonId = radioGroupPasto.checkedRadioButtonId
            val radioButton = view?.findViewById<RadioButton>(radioButtonId)
            val scelta = radioButton?.text.toString()
            chosenMeal = scelta
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
                    prenotaRistorante()
                }
            }

        }

        return binding.root
    }

    private fun prenotaRistorante() {
            val numberOfGuest = selectedGuests
            val resturantDate = selectedDate
            val sceltaPasto = chosenMeal
            val idRicevuto = idUtente

            textNotification = "Hai prenotato il ristorante per la data $selectedDate"

            if (numberOfGuest>0 && resturantDate!=null && sceltaPasto !=null){
                val requestResturant =
                    idRicevuto?.let { RequestResturant(it,numberOfGuest,resturantDate,sceltaPasto) }
                requestResturant?.let { prenotazioneRistorante(it) }
            }else{
                Toast.makeText(
                    requireContext(),
                    "Non hai completato i campi richiesti",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
        val ospitiPickerButton: Button = binding.ospitiPickerRistorante
        ospitiPickerButton.text = "Numero di ospiti: $selectedGuests"
    }


    private fun showDatePicker() {
        val currentDate = LocalDate.now()
        val currentYear = currentDate.year
        val currentMonth = currentDate.monthValue
        val currentDay = currentDate.dayOfMonth

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                this@RistoranteFragment.selectedDate = LocalDate.of(year, month + 1, day)

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

    private fun prenotazioneRistorante(requestResturant: RequestResturant){
        val query = "INSERT INTO prenotazioneRistorante(id_utente, data_prenotazione, numero_ospiti, chosen_meal)" +
        "VALUES (${requestResturant.idUtente}, '${requestResturant.resturantDate}', ${requestResturant.numberOfGuest}, '${requestResturant.scelta}');"
        Log.d("QUERY","La mia query è: ${query}")

        ClientNetwork.retrofit.insertResturantReservation(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {
                        val dateString = requestResturant.resturantDate.toString()
                       dbManager.insertPrenotazioneRistorante(requestResturant.idUtente,dateString,
                       requestResturant.numberOfGuest,requestResturant.scelta)
                        Toast.makeText(
                            requireContext(),
                            "Prenotazione effettuata",
                            Toast.LENGTH_SHORT
                        ).show()
                        inserisciNotificaRistorante()
                    } else {
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
                    Log.i("LOG-Resturant_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun inserisciNotificaRistorante(){
        val query = "INSERT INTO notifications (ref_notification, type, message) " +
                "VALUES ($idUtente, '$typeNotification', '$textNotification');"

        ClientNetwork.retrofit.inserResturantNotification(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Log.d("NOTIFICA","NOTIFICA INSERITA")
                    }else{
                        Log.d("PROBLEMA","PROBLEMA")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Resturant_Fragment-onFailure", "Errore ${t.message}")
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
            Log.i("LOG-RistoranteFragment-onFailure", "Errore accesso ${e.message}")
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        Log.d("RETURN","Il return è : $reservationExists")

        return reservationExists
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }


}
