package com.example.seasonapp.profilazione


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.databinding.FragmentModificaProfiloBinding

class ModificaProfiloFragment : Fragment() {
    private lateinit var binding: FragmentModificaProfiloBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentModificaProfiloBinding.inflate(inflater, container, false)
        val view = binding.root

        val buttonIndietro = binding.buttonIndietroModifica
        buttonIndietro.setOnClickListener {
            findNavController().navigate(R.id.action_modificaProfiloFragment_to_profileFragment)
        }

        val nome = "Nome"  //da settare con il db
        val nomeUtente = getString(R.string.nomeUtenteLoggato, nome)
        binding.textNome.text = nomeUtente

        val cognome = "Cognome"  //da settare con il db
        val cognomeUtente = getString(R.string.cognomeUtenteLoggato, cognome)
        binding.textCognome.text = cognomeUtente

        val dNascita = "Data di nascita"  //da settare con il db
        val dataUtente = getString(R.string.dataNascitaUtenteLoggato, dNascita)
        binding.textDataNascita.text = dataUtente

        return view
    }

}