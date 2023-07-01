package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.databinding.FragmentDomandaSicurezzaBinding
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding

class DomandaSicurezzaFragment : Fragment() {
    private lateinit var binding: FragmentDomandaSicurezzaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentDomandaSicurezzaBinding.inflate(inflater,container,false)
        val view = binding.root
        val domandaSicurezza= "domandaSicurezza"  //da settare con il db
        val domanda = getString(R.string.domandaSicurezzaUtente, domandaSicurezza)
        binding.domandaSicurezzaText.text = domanda

        val buttonIndietro = binding.buttonIndietroRecupero
        buttonIndietro.setOnClickListener {

            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_recuperoUsernameFragment)

        }
        val buttonContinua = binding.buttonContinuaRecuperoUser
        buttonContinua.setOnClickListener {

            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_loginFragment)

        }
        return view
    }
}