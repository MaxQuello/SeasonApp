package com.example.seasonapp.profilazione

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R


class RecuperoPasswordFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_recupero_password, container, false)
        val buttonContinua = view.findViewById<Button>(R.id.buttonContinuaRecuperoPw)
        val buttonindietro = view.findViewById<ImageButton>(R.id.buttonIndietro)

        buttonindietro.setOnClickListener {
            findNavController().navigate(R.id.action_recuperoPasswordFragment_to_loginFragment)

        }

        buttonContinua.setOnClickListener {
            //QUERY DB PER CONTROLLARE SE EMAIL E N TELEFONO SONO CORRETTI
            //SE SONO CORRETTI GENERA NUMERO DA TOT CIFRE E LO MANDA AL DB E LO STAMPA SULLO SCHERMO
            // CON UN TOAST, SUCCESSIVAMENTE APPARE IL FRAGMENT COME QUI SOTTO
            findNavController().navigate(R.id.action_recuperoPasswordFragment_to_otpFragment)

        }
        return view
    }

}