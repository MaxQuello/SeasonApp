package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentRecuperoPasswordBinding


class RecuperoPasswordFragment : Fragment() {
    private lateinit var binding: FragmentRecuperoPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentRecuperoPasswordBinding.inflate(layoutInflater)
        return binding.root

    }
}