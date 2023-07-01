package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    var idUtente : Int? = null
    private lateinit var dbManager: DbManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.buttonAccedi
        binding.buttonRegistrati

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()

        val nomeUtente = "$username"  //da settare con il db
        val messaggioBenvenuto = getString(R.string.benvenuto, nomeUtente)
        binding.testoProfilo.text = messaggioBenvenuto

        val login = binding.buttonAccedi
        login.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
        val registrati = binding.buttonRegistrati
        registrati.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_registratiFragment)
        }

        val prenotazioni = binding.layoutButtonLeMiePrenotazioni
        prenotazioni.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_gestisciPrenotazioniFragment)

        }

        val modifica = binding.layoutButtonModificaProfilo
        modifica.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_modificaProfiloFragment)

        }

        val servizi = binding.layoutButtonIMieiServizi
        servizi.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_gestisciServiziFragment)

        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }

}