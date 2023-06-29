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


class OtpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp, container, false)
        val buttonIndietro = view.findViewById<ImageButton>(R.id.buttonIndietro3)
        buttonIndietro.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_recuperoPasswordFragment)

        }
        //QUERY DB PER CONTROLLARE ESATTEZZA DELL'OTP INSERITO
        val buttonContinua = view.findViewById<Button>(R.id.buttonContinua)
        buttonContinua.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_loginFragment)

        }
        return view
    }

}