package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding


class RecuperoUsernameFragment : Fragment() {
    private lateinit var binding: FragmentRecuperoUsernameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentRecuperoUsernameBinding.inflate(layoutInflater)
        return binding.root

    }
}