package com.example.seasonapp.profilazione

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding
import com.example.seasonapp.model.InsertOtp
import com.example.seasonapp.model.RequestOtp
import com.example.seasonapp.model.RequestUsername
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecuperoUsernameFragment : Fragment() {
    private lateinit var binding: FragmentRecuperoUsernameBinding
    private lateinit var dbManager: DbManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecuperoUsernameBinding.inflate(inflater,container,false)
        val view = binding.root
        dbManager = DbManager(requireContext())
        dbManager.open()

        val buttonIndietro = binding.buttonIndietroRecuperoUsername
        buttonIndietro.setOnClickListener {

            findNavController().navigate(R.id.action_recuperoUsernameFragment_to_loginFragment)

        }
        val buttonContinua = binding.buttonContinuaRecuperoUs
        buttonContinua.setOnClickListener {
            val email = binding.emailText.text.toString()
            val telefono = binding.inserisciNumero2.text.toString()
            val requestUsername = RequestUsername(email = email, nTelefono = telefono)
            verificaEsistenzaUtente(requestUsername)
        }
        return view
    }

    private fun verificaEsistenzaUtente(requestUsername: RequestUsername) {
        val query =
            "SELECT * FROM utente WHERE email = '${requestUsername.email}' AND numeroDiTelefono = '${requestUsername.nTelefono}';"
        Log.d("DEBUG", "La tua query sarà: ${query}")
        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i(
                        "onResponse",
                        "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}"
                    )
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {

                        findNavController().navigate(R.id.action_recuperoUsernameFragment_to_domandaSicurezzaFragment)


                    } else {
                        val errorMessage = response.message()
                        Log.e("onResponse", "Nessun utente trovato: $errorMessage")
                        Toast.makeText(
                            requireContext(),
                            "Nessun utente trovato",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Recupero_Password_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )

    }
}