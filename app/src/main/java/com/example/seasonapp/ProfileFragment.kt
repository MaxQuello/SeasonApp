package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.databinding.FragmentLoginBinding
import com.example.seasonapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var buttonAccedi: Button
    private lateinit var buttonRegistrati: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.buttonAccedi
        binding.buttonRegistrati

        val nomeUtente = "user"  //da settare con il db
        val messaggioBenvenuto = getString(R.string.benvenuto, nomeUtente)
        binding.testoProfilo.text = messaggioBenvenuto

        val login = view.findViewById<Button>(R.id.buttonAccedi)
        login.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
        val registrati = view.findViewById<Button>(R.id.buttonRegistrati)
        registrati.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_registratiFragment)
        }

        return binding.root

    }
}