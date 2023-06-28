package com.example.seasonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.databinding.FragmentRecensioniBinding
import com.google.gson.JsonObject

class RecensioniFragment : Fragment() {
    private lateinit var binding: FragmentRecensioniBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecensioniBinding.inflate(layoutInflater)

        //INSERIRE QUERY DB PER L'INSERIMENTO DELLE RECENSIONI
        // E PER PRENDERE LE RECENSIONI CHE SONO NEL DB PER POPOLARE LE CARDVIEW

        return binding.root

    }

}