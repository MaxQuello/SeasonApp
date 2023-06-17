package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController


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
            findNavController().navigate(R.id.action_recuperoPasswordFragment_to_otpFragment)

        }
        return view
    }
}