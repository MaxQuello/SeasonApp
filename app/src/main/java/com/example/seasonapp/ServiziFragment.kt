package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.example.seasonapp.databinding.FragmentServiziBinding

class ServiziFragment : Fragment() {
    private lateinit var binding: FragmentServiziBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View {
        binding = FragmentServiziBinding.inflate(layoutInflater)
        return binding.root
    }
}