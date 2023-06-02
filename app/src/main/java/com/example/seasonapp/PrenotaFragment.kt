package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentPrenotaBinding

class PrenotaFragment : Fragment() {
    private lateinit var binding: FragmentPrenotaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
                binding = FragmentPrenotaBinding.inflate(layoutInflater)
                return binding.root
        }
    }
