package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.example.seasonapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var ratingBar: RatingBar
    private lateinit var binding: FragmentHomeBinding

    private fun onClickPrenota() {
        val manager = parentFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment,PrenotaFragment())
        transaction.commit()
        transaction.addToBackStack(null)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        ratingBar = binding.ratingBar

        ratingBar.setIsIndicator(true)

        binding.buttonPrenota.setOnClickListener {
            onClickPrenota()
        }

        return binding.root


    }

}