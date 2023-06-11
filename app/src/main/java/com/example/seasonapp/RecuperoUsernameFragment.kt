package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding


class RecuperoUsernameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_recupero_username, container, false)
        val buttonindietro = view.findViewById<Button>(R.id.buttonIndietro2)
        buttonindietro.setOnClickListener {
            findNavController().navigate(R.id.action_recuperoUsernameFragment_to_loginFragment)

        }
        return view
    }
}