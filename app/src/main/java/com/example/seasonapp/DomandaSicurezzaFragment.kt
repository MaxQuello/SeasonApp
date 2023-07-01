package com.example.seasonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.databinding.FragmentDomandaSicurezzaBinding
import com.example.seasonapp.databinding.FragmentRecuperoUsernameBinding
import com.example.seasonapp.model.RequestDomanda
import com.example.seasonapp.model.RequestUsername
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DomandaSicurezzaFragment : Fragment() {
    private lateinit var binding: FragmentDomandaSicurezzaBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentDomandaSicurezzaBinding.inflate(inflater, container, false)
        val view = binding.root
        val domandaSicurezza = "domandaSicurezza"  //da settare con il db
        val domanda = getString(R.string.domandaSicurezzaUtente, domandaSicurezza)
        binding.domandaSicurezzaText.text = domanda

        val buttonIndietro = binding.buttonIndietroRecupero
        buttonIndietro.setOnClickListener {

            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_recuperoUsernameFragment)

        }
        val buttonContinua = binding.buttonContinuaRecuperoUser
        buttonContinua.setOnClickListener {

            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_loginFragment)

        }
        return view
    }

    private fun verificaDomanda(requestDomanda: RequestDomanda) {
        val query =
            "SELECT username FROM utente WHERE domandaDiSicurezza = '${requestDomanda.domanda}' AND risposta = '${requestDomanda.risposta}';"
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

                        val username = "???"
                        findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_loginFragment)
                        Toast.makeText(context, "Il tuo Username è '${username}'", Toast.LENGTH_SHORT).show()

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