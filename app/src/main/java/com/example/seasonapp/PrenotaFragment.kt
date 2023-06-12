package com.example.seasonapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PrenotaFragment : Fragment() {
    private lateinit var binding: FragmentPrenotaBinding
    private lateinit var datePickerButton : Button
    private var selectedCheckInDate: Date? = null
    private var selectedCheckOutDate: Date? = null
    private lateinit var guestSelection :Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
                binding = FragmentPrenotaBinding.inflate(layoutInflater)
                datePickerButton = binding.datePicker
                datePickerButton.setOnClickListener {
                    showDatePicker()
                }
                guestSelection = binding.locationPicker
                guestSelection.setOnClickListener {
                    showGuestsSelectionDialog()
                }

                return binding.root
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

                if (selectedCheckInDate == null) {
                    selectedCheckInDate = selectedDate.time
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
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Imposta la data minima selezionabile nel DatePickerDialog
        selectedCheckInDate?.let { datePickerDialog.datePicker.minDate = it.time }

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

        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Seleziona il numero di ospiti")
            .setView(guestsNumberPicker)
            .setPositiveButton("OK") { dialog, _ ->
                val selectedGuests = guestsNumberPicker.value
                // Esegui l'azione desiderata con il numero di ospiti selezionato
                dialog.dismiss()
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }



}
