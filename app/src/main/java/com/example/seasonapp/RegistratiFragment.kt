package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentLoginBinding
import com.example.seasonapp.databinding.FragmentRegistratiBinding


class RegistratiFragment : Fragment() {
    private lateinit var binding: FragmentRegistratiBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentRegistratiBinding.inflate(inflater,container,false)

        return binding.root

    }
}