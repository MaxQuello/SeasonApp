package com.example.seasonapp

import AdapterOfferte
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.model.RequestRoom
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects


class PrenotaFragment : Fragment() {
    private lateinit var binding: FragmentPrenotaBinding
    private lateinit var datePickerButton : Button
    private lateinit var searchButton: Button
    private var selectedCheckInDate: Date? = null
    private var selectedCheckOutDate: Date? = null
    private var checkInSelected = false
    private lateinit var guestSelection :Button
    private var selectedGuests = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        var date: List<LocalDate?>? = null
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

                searchButton = binding.search
                searchButton.setOnClickListener {
                    if (!checkiflogindone()){  //togliere ! quando il db è linkato bene
                        date = searchRoom()
                        //preleva dati db e popola arrayList offerte
                        val offerte = ArrayList<Offerta>()
                        for(i in 1..3){ //qui sostituire il for con un for migliorato che itera il json
                            offerte.add(Offerta("Bellissime",99, 999.99, date?.get(0), date?.get(1), 99)) // qui dentro andranno gli attributi del risultato della query per popolare la cardview
                        }

                        binding.listaOfferte.adapter = AdapterOfferte(offerte)
                        binding.listaOfferte.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                    }else{
                        //Mi porta al profilo
                    }
                }

                return binding.root
        }

    private fun searchRoom(): List<LocalDate?>? {
        val numberOfGuests = selectedGuests
        val checkInDate = selectedCheckInDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        val checkOutDate = selectedCheckOutDate?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
        Log.i("MyTag", checkInDate.toString())

        // Verifica se le informazioni sono valide
        if (numberOfGuests > 0 && checkInDate != null && checkOutDate != null) {
            val requestRoom = RequestRoom(checkInDate,checkOutDate,numberOfGuests)

            ricercaCamereDB()
            return listOf(checkInDate, checkOutDate)
        } else {
            Toast.makeText(
                requireContext(),
                "Completa tutte le informazioni di prenotazione",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
    }

    private fun ricercaCamereDB() {
        val query = "select * from "

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
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)

                if (selectedDate.time.compareTo(calendar.time) < 0) {
                    // La data di check-in è precedente alla data corrente
                    // Mostra un messaggio di errore o prendi un'altra azione appropriata
                    Toast.makeText(
                        requireContext(),
                        "La data di Check-in non può essere precedente alla data corrente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (!checkInSelected) {
                        selectedCheckInDate = selectedDate.time
                        checkInSelected = true
                        showDatePicker()
                    } else {
                        if (selectedDate.time.compareTo(selectedCheckInDate!!) > 0) {
                            selectedCheckOutDate = selectedDate.time
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
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun updateButtonWithSelectedDates() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val checkInDateString = selectedCheckInDate?.let { dateFormat.format(it) } ?: ""
        val checkOutDateString = selectedCheckOutDate?.let { dateFormat.format(it) } ?: ""

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
}
