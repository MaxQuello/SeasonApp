package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var recuperoPassword: TextView
    private lateinit var recuperoUsername: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        recuperoPassword = binding.recuperoPassword

        binding.recuperoPassword.setOnClickListener {
            when (R.id.recuperoPassword) {
                R.id.recuperoPassword -> (RecuperoPasswordFragment())
            }

        }

        recuperoUsername = binding.recuperoUsername

        binding.recuperoPassword.setOnClickListener {
            when (R.id.recuperoUsername) {
                R.id.recuperoUsername -> (RecuperoUsernameFragment())
            }

        }

        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root

    }

}