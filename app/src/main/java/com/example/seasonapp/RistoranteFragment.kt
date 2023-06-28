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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentRistoranteBinding
import com.example.seasonapp.model.RequestResturant
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
    val idUtente = SessionManager.userId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRistoranteBinding.inflate(layoutInflater)

        dbManager = DbManager(requireContext())
        dbManager.open()



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
            prenotaRistorante()

            Log.d("Prova","pasto scelto: ${chosenMeal}")
        }

        return binding.root
    }

    private fun prenotaRistorante() {
        if(idUtente != null){
            val numberOfGuest = selectedGuests
            val resturantDate = selectedDate
            val sceltaPasto = chosenMeal
            val idRicevuto = idUtente

            if (numberOfGuest>0 && resturantDate!=null && sceltaPasto !=null){
                val requestResturant = RequestResturant(idRicevuto,numberOfGuest,resturantDate,sceltaPasto)
                prenotazioneRistorante(requestResturant)
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
                       /*dbManager.insertPrenotazioneRistorante(requestResturant.resturantDate.toString(),requestResturant.numberOfGuest
                       ,requestResturant.scelta)*/
                        Toast.makeText(
                            requireContext(),
                            "Prenotazione effettuata",
                            Toast.LENGTH_SHORT
                        ).show()
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
}
