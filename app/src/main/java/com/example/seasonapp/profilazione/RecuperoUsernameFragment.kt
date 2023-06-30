package com.example.seasonapp.profilazione

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding


class RecuperoUsernameFragment : Fragment() {
    private lateinit var binding: FragmentRecuperoUsernameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecuperoUsernameBinding.inflate(inflater,container,false)
        val view = binding.root
        val domandaSicurezza= "domandaSicurezza"  //da settare con il db
        val domanda = getString(R.string.domandaSicurezzaUtente, domandaSicurezza)
        binding.domandaSicurezzaText.text = domanda

        val buttonIndietro = binding.buttonIndietroRecuperoUsername
        buttonIndietro.setOnClickListener {

            findNavController().navigate(R.id.action_recuperoUsernameFragment_to_loginFragment)

        }
        val buttonContinua = binding.buttonContinuaRecuperoUs
        buttonContinua.setOnClickListener {

            findNavController().navigate(R.id.action_recuperoUsernameFragment_to_loginFragment)

        }
        return view
    }
}