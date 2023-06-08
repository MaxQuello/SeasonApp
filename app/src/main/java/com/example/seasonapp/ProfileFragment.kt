package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        val nomeUtente = "nomeUtente"  //da settare con il db
        val messaggioBenvenuto = getString(R.string.benvenuto, nomeUtente)

        binding.testoProfilo.text = messaggioBenvenuto

        return binding.root

    }
}