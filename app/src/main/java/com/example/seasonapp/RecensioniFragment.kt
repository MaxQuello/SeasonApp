package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentRecensioniBinding

class RecensioniFragment : Fragment() {
    private lateinit var binding: FragmentRecensioniBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecensioniBinding.inflate(layoutInflater)

        return binding.root

    }

}