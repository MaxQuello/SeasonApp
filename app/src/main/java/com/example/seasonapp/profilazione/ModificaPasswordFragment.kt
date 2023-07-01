package com.example.seasonapp.profilazione

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seasonapp.databinding.FragmentModificaPasswordBinding


class ModificaPasswordFragment : Fragment() {
    private lateinit var binding: FragmentModificaPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentModificaPasswordBinding.inflate(layoutInflater)
        return binding.root

    }
}