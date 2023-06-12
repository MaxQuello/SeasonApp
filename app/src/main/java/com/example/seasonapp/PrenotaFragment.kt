package com.example.seasonapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    private var isSelectingCheckInDate = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
                binding = FragmentPrenotaBinding.inflate(layoutInflater)
                datePickerButton = binding.datePicker
                datePickerButton.setOnClickListener {
                    showDatePicker()
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

                if (selectedDate.timeInMillis < System.currentTimeMillis()) {
                    Toast.makeText(
                        requireContext(),
                        "Non puoi selezionare una data passata",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@DatePickerDialog
                }

                if (isSelectingCheckInDate) {
                    selectedCheckInDate = selectedDate.time
                } else {
                    if (selectedDate.time.compareTo(selectedCheckInDate!!) > 0) {
                        selectedCheckOutDate = selectedDate.time
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "La data di Check-out deve essere successiva alla data di Check-in",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                isSelectingCheckInDate = !isSelectingCheckInDate
                updateButtonWithSelectedDates()
            },
            currentYear,
            currentMonth,
            currentDay
        )

        if (!isSelectingCheckInDate) {
            selectedCheckInDate?.let { datePickerDialog.datePicker.minDate = it.time }
        }

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }


    private fun updateButtonWithSelectedDates() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val checkInDateString = selectedCheckInDate?.let { dateFormat.format(it) } ?: ""
        val checkOutDateString = selectedCheckOutDate?.let { dateFormat.format(it) } ?: ""

        val buttonText = "$checkInDateString - $checkOutDateString"
        datePickerButton.text = buttonText
    }

}
