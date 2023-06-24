package com.example.seasonapp
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import com.example.seasonapp.databinding.FragmentRistoranteBinding
import java.time.LocalDate


class RistoranteFragment : Fragment() {
    private lateinit var binding: FragmentRistoranteBinding
    private lateinit var datePickerButton: Button
    private lateinit var guestButton: Button
    private var selectedDate: LocalDate? = null
    private var selectedGuests = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRistoranteBinding.inflate(layoutInflater)

        datePickerButton = binding.datePickerRistorante
        datePickerButton.setOnClickListener {
            showDatePicker()
        }

        guestButton = binding.ospitiPickerRistorante
        guestButton.setOnClickListener {
            showGuestsSelectionDialog()
        }

        return binding.root
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
}
