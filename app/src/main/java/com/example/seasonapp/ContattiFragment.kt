package com.example.seasonapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentContattiBinding

class ContattiFragment : Fragment() {
      private lateinit var binding: FragmentContattiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContattiBinding.inflate(layoutInflater)
        val chiamaci = R.id.contattaciButton
        binding.contattaciButton.setOnClickListener { menuItem ->
            Log.i("mytag", "funziono")
            when (R.id.contattaciButton) {
                R.id.contattaciButton -> {
                    Log.i("mytag", "funziono veramente")
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${getString(R.string.numeroTelefono)}")
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        binding = FragmentContattiBinding.inflate(layoutInflater)
        return binding.root
    }

}