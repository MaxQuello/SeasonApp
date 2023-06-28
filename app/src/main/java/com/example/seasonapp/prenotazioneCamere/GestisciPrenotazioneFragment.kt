package com.example.seasonapp.prenotazioneCamere

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentGestisciPrenotazioneBinding


class GestisciPrenotazioneFragment : Fragment() {
    private lateinit var binding: FragmentGestisciPrenotazioneBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentGestisciPrenotazioneBinding.inflate(layoutInflater)
        return binding.root

    }
}