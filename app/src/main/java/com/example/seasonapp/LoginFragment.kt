package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val recuperopass = view.findViewById<TextView>(R.id.recuperoPassword)
        recuperopass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperoPasswordFragment)
        }
        val recuperouser = view.findViewById<TextView>(R.id.recuperoUsername)
        recuperouser.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperoUsernameFragment)
        }

        return view

    }

}