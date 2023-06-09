package com.example.seasonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var ratingBar: RatingBar
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        ratingBar = binding.ratingBar

        ratingBar.setIsIndicator(true)

        return binding.root

    }

}