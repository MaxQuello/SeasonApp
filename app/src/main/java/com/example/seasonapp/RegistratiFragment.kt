package com.example.seasonapp

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.databinding.FragmentRegistratiBinding


class RegistratiFragment : Fragment() {
    private lateinit var binding: FragmentRegistratiBinding
    private lateinit var multiSelectSpinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentRegistratiBinding.inflate(layoutInflater)
        multiSelectSpinner = binding.multiSelectSpinner
        val options = arrayOf("Cognome da nubile di tua madre", "La tua squadra del cuore", "Nome del tuo animale domestico")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        multiSelectSpinner.adapter = adapter

        val buttonIndietro = binding.buttonIndietroRegistrati
        buttonIndietro.setOnClickListener {
            findNavController().navigate(com.example.seasonapp.R.id.action_registratiFragment_to_profileFragment)

        }

        return binding.root

    }
}