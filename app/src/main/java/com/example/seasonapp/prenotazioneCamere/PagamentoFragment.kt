package com.example.seasonapp.prenotazioneCamere

import AdapterOfferte
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.Offerta
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentPagamentoBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class PagamentoFragment : Fragment() {
    private lateinit var binding: FragmentPagamentoBinding
    private var listaOfferte: ArrayList<Offerta>? = null
    private lateinit var postiAutoButton: Button
    private var selectedCar: Int = 0
    private lateinit var confermaPagamentoButton: Button
    private var costoTotale = 0.0
    private var costoMacchina = 10.0
    private var roomId : Int? = null
    private var dataCheckIn : LocalDate? = null
    private var dataCheckOut: LocalDate? = null
    private var numerogiorni : Int? = null
    private lateinit var editTextSconto : EditText
    private lateinit var buttonSconto : Button

    var idUtente : Int? = null
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listaOfferte = arguments?.getParcelableArrayList<Offerta>("offerte")?.let {
            ArrayList(it)
        }
        dataCheckIn = listaOfferte?.get(0)?.dataCheckIn
        dataCheckOut = listaOfferte?.get(0)?.dataCheckOut

        numerogiorni = ChronoUnit.DAYS.between(dataCheckIn,dataCheckOut).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagamentoBinding.inflate(inflater, container, false)
        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()
        idUtente = username?.let { dbManager.getUserIdByUsername(it) }
        binding.listaOffertePagamento.adapter = listaOfferte?.let { AdapterOfferte(it) }
        binding.listaOffertePagamento.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        postiAutoButton = binding.postiAutoPicker

        // Inizializza la TextView con un valore di default
        binding.prezzoFinale.text = ""

        postiAutoButton.setOnClickListener {
            showGuestsSelectionDialog()
        }

        editTextSconto = binding.editTextVoucher

        buttonSconto = binding.buttonVoucher
        buttonSconto.setOnClickListener {
            val codice = editTextSconto.text.toString()
            if (codice != ""){
                val query = "SELECT 1 FROM utente WHERE codice = '${codice}' AND id = ${idUtente} "
                ClientNetwork.retrofit.getIdUtente(query).enqueue(
                    object  : Callback<JsonObject>{
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if (response.isSuccessful){
                                val risultato = response.body()?.getAsJsonArray("queryset")
                                if (risultato != null){
//
                                }
                            }
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    }
                )
            }

        }



        confermaPagamentoButton = binding.buttonPaga
        confermaPagamentoButton.setOnClickListener {
            for (offerte in listaOfferte!!){
                roomId = offerte.roomId
                dataCheckIn = offerte.dataCheckIn
                dataCheckOut = offerte.dataCheckOut
            }


            updatePrezzoFinale() // Aggiorna il prezzo finale
            inserisciCamera(roomId,dataCheckIn,dataCheckOut)
        }

        return binding.root
    }

    private fun inserisciCamera(roomId: Int?, dataCheckIn: LocalDate?, dataCheckOut: LocalDate?) {
        // Esegue la query per inserire la camera nel database
        val query = "INSERT INTO reservations (roomId, checkInDate, checkOutDate, ref_reservations, costo) " +
                "VALUES ($roomId, '$dataCheckIn', '$dataCheckOut', $idUtente, $costoTotale);"

        ClientNetwork.retrofit.addReservationRoom(query).enqueue(
            object : Callback<JsonObject> {
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
                    Log.i("LOG-Prenota_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun showGuestsSelectionDialog() {
        val guestsNumberPicker = NumberPicker(context)
        guestsNumberPicker.minValue = 1
        guestsNumberPicker.maxValue = 5
        guestsNumberPicker.value = selectedCar

        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Seleziona il numero di ospiti")
            .setView(guestsNumberPicker)
            .setPositiveButton("OK") { dialog, _ ->
                selectedCar = guestsNumberPicker.value
                updateOspitiPickerButtonText()
                updatePrezzoFinale() // Aggiorna il prezzo finale dopo aver selezionato il numero di macchine
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun updateOspitiPickerButtonText() {
        val ospitiPickerButton: Button = binding.postiAutoPicker
        ospitiPickerButton.text = "Numero di macchine: $selectedCar"
    }

    private fun updatePrezzoFinale() {
        val textViewPrezzo = binding.textPrezzo

        // Calcola il prezzo finale solo se è stato selezionato il numero di macchine
        if (selectedCar > 0) {
            costoTotale = 0.0
            for (offerta in listaOfferte!!) {
                costoTotale += offerta.prezzo
            }

            costoTotale += costoMacchina * selectedCar // Aggiorna il prezzo totale con il costo delle macchine
            val prezzoFinale = costoTotale * numerogiorni!!
            textViewPrezzo.text = "PREZZO FINALE: $prezzoFinale"
            textViewPrezzo.visibility = View.VISIBLE // Rendi la TextView visibile
        } else {
            textViewPrezzo.visibility = View.GONE // Nascondi la TextView se il numero di macchine non è stato selezionato
        }
    }

}


