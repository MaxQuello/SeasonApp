package com.example.seasonapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.databinding.FragmentServiziBinding
import com.example.seasonapp.model.RequestGym
import com.example.seasonapp.model.RequestResturant
import com.google.gson.JsonObject
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
    val idUtente = SessionManager.userId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View {
        binding = FragmentServiziBinding.inflate(layoutInflater)

        Log.d("Prova","L'id Utente è : ${idUtente}")

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
            prenotaPalestra()
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
        if(idUtente != null){
            val numberOfGuest = selectedGuests
            val gymDate = selectedDate
            val idRicevuto = idUtente

            if (numberOfGuest>0 && gymDate!=null){
                val requestGym = RequestGym(idRicevuto,gymDate,numberOfGuest)
                prenotazionePalestra(requestGym)
            }else{
                Toast.makeText(
                    requireContext(),
                    "Non hai completato i campi richiesti",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
            Toast.makeText(
                requireContext(),
                "Devi fare l'accesso per prenotare",
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
}