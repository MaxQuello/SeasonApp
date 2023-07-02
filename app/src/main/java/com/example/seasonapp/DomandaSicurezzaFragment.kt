package com.example.seasonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.SessionManager
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
    private lateinit var emailEditText: EditText
    private var domandaString : String? = null
    private var email : String? = null
    private lateinit var rispostaEditText: EditText
    private var rispostaString : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentDomandaSicurezzaBinding.inflate(inflater, container, false)
        val view = binding.root


        val sessionManager = SessionManager.getInstance(requireContext())
        email = sessionManager.getEmail()
        getDomandaSicurezza()

        val buttonIndietro = binding.buttonIndietroRecupero
        buttonIndietro.setOnClickListener {
            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_recuperoUsernameFragment)
        }
        val buttonContinua = binding.buttonContinuaRecuperoUser
        buttonContinua.setOnClickListener {
            rispostaEditText = binding.rispostaText
            rispostaString = rispostaEditText.text.toString()
            verificaDomanda(rispostaString!!, email!!)
            findNavController().navigate(R.id.action_domandaSicurezzaFragment_to_loginFragment)

        }
        return view
    }

    private fun getDomandaSicurezza() {
        val query = "SELECT domanda FROM utente WHERE mail = '${email}'"
        Log.d("QUERY","QUERY: ${query}")
        ClientNetwork.retrofit.getDomanda(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("ON RESPONSE","Sono dentro la onResponse l'esito sara ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val bodyString = response.body()
                        Log.d("BODY","BODY: ${bodyString}")
                        val jsonArray = bodyString?.getAsJsonArray("queryset")

                        if (jsonArray != null && jsonArray.size() > 0) {
                            val jsonObject = jsonArray.get(0)?.asJsonObject
                            domandaString = jsonObject?.getAsJsonPrimitive("domanda")?.asString

                            if (domandaString != null) {
                                val domandaSicurezza = domandaString
                                val domanda = getString(R.string.domandaSicurezzaUtente, domandaSicurezza)
                                binding.domandaSicurezzaText.text = domanda
                            } else {
                                // La chiave "domanda" non è presente o non è di tipo stringa, gestisci l'errore
                                Log.d("ERRORE", "Chiave \"domanda\" mancante o non è di tipo stringa")
                            }
                        } else {
                            // L'array è vuoto o non è stato trovato, gestisci l'errore
                            Log.d("ERRORE", "Array vuoto o non trovato")
                        }
                    } else {
                        // La chiamata non è stata riuscita, gestisci l'errore
                        Log.d("ERRORE", "Chiamata non riuscita con codice: " + response.code())
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Domanda_Sicurezza_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun verificaDomanda(rispostaString: String,email:String) {
        val query =
            "SELECT username FROM utente WHERE domanda = '${domandaString}' AND risposta = '${rispostaString}' " +
                    "AND mail = '${email}';"
        Log.d("QUERY","QUERY: ${query}")
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
                        val bodyString = response.body()
                        Log.d("BODY", "BODY: ${bodyString}")
                        val jsonArray = bodyString?.getAsJsonArray("queryset")

                        if (jsonArray != null && jsonArray.size() > 0) {
                            val jsonObject = jsonArray.get(0)?.asJsonObject
                            val username = jsonObject?.getAsJsonPrimitive("username")?.asString

                            if (username != null) {
                                val message = "Il tuo Username è '$username'"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            } else {
                                // La chiave "username" non è presente o non è di tipo stringa, gestisci l'errore
                                Log.d("ERRORE", "Chiave \"username\" mancante o non è di tipo stringa")
                            }
                        } else {
                            // L'array è vuoto o non è stato trovato, gestisci l'errore
                            Log.d("ERRORE", "Array vuoto o non trovato")
                        }
                    } else {
                        // La chiamata non è stata riuscita, gestisci l'errore
                        val errorMessage = response.message()
                        Log.e("onResponse", "Nessun utente trovato: $errorMessage")
                        Toast.makeText(requireContext(), "Nessun utente trovato", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Domanda_Sicurezza_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )


    }

}