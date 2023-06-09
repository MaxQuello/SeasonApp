package com.example.seasonapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentContattiBinding

class ContattiFragment : Fragment() {
    private lateinit var binding: FragmentContattiBinding
    private lateinit var contattaciButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContattiBinding.inflate(inflater, container, false)
        contattaciButton = binding.contattaciButton

        contattaciButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${getString(R.string.numeroTelefono)}")
            startActivity(intent)
        }

        return binding.root
    }
}
