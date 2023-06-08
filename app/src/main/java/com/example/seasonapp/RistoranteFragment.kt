package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.databinding.FragmentRistoranteBinding

class RistoranteFragment : Fragment() {
    private lateinit var binding: FragmentRistoranteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View {
        binding = FragmentRistoranteBinding.inflate(layoutInflater)
        return binding.root
    }
}