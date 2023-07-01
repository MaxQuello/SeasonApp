package com.example.seasonapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentLoginBinding
import com.example.seasonapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbManager: DbManager
    var idUtente : Int? = null
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
        if(checkiflogindone()){
            val sessionManager = SessionManager.getInstance(requireContext())
            val username = sessionManager.getUsername()
            val nomeUtente = "$username"  //da settare con il db
            val messaggioBenvenuto = getString(R.string.benvenuto, nomeUtente)
            binding.testoProfilo.text = messaggioBenvenuto
        }else{
            val nomeUtente = "user"
            val messaggioBenvenuto = getString(R.string.benvenuto, nomeUtente)
            binding.testoProfilo.text = messaggioBenvenuto
        }




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

    private fun checkiflogindone(): Boolean {
        // Ottenere un'istanza delle SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Recuperare lo stato del login
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Verificare lo stato del login
        if (isLoggedIn) {
            binding.conditionLayout.visibility = View.GONE
            binding.buttonLayout.visibility = View.VISIBLE
            Log.i("PROVA","LOGIN FATTO")
            return true
        } else {
            binding.conditionLayout.visibility = View.VISIBLE
            binding.buttonLayout.visibility = View.GONE
            Log.i("PROVA","LOGIN NON FATTO")
            return false
        }

    }


}