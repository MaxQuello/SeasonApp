package com.example.seasonapp.profilazione

import android.database.Cursor
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
import com.example.seasonapp.data.DBHelper
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentRecuperoPasswordBinding
import com.example.seasonapp.model.InsertOtp
import com.example.seasonapp.model.RequestOtp
import com.example.seasonapp.model.RequestRegistration
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.SecureRandom
import kotlin.random.Random


class RecuperoPasswordFragment : Fragment() {
    private lateinit var binding: FragmentRecuperoPasswordBinding
    private lateinit var dbManager: DbManager
    var username = ""
    var telefono = ""
    var idUtente : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecuperoPasswordBinding.inflate(layoutInflater)
        dbManager = DbManager(requireContext())
        dbManager.open()


        val view = binding.root
        val insertUsername = binding.inserisciUsername
        val insertTelefono = binding.inserisciNumero



        binding.buttonIndietro.setOnClickListener {
            findNavController().navigate(R.id.action_recuperoPasswordFragment_to_loginFragment)

        }

        binding.buttonContinuaRecuperoPw.setOnClickListener {
            if (insertUsername.text.toString() != ""  && insertTelefono.text.toString() != ""){
                val username = binding.inserisciUsername.text.toString()
                val telefono = binding.inserisciNumero.text.toString()
                val requestOtp = RequestOtp(username = username, nTelefono = telefono)
                verificaEsistenzaUtente(requestOtp)

            }else{
                Log.i("LOG-Login_Fragment", "L'utente non ha inserito le credenziali")
                Toast.makeText(context,"Inserisci i dati mancanti", Toast.LENGTH_LONG).show()
            }

        }
        return view
    }

    private fun verificaEsistenzaUtente(requestOtp: RequestOtp) {
        val query =
            "SELECT id FROM utente WHERE username = '${requestOtp.username}' AND numeroDiTelefono = '${requestOtp.nTelefono}';"
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

                        findNavController().navigate(R.id.action_recuperoPasswordFragment_to_otpFragment)
                        val otp = generaOtp()

                        Toast.makeText(
                            requireContext(),
                            "Il tuo codice di recupero è ${otp}",
                            Toast.LENGTH_LONG
                        ).show()


                        val jsonObject = bodyString?.getAsJsonArray("queryset")?.get(0)?.asJsonObject
                        idUtente = jsonObject?.getAsJsonPrimitive("id")?.asInt
                        val sessionManager = SessionManager.getInstance(requireContext())
                        sessionManager.setUsername(requestOtp.username)
                        Log.d("USERNAME","L'USERNAME E' : ${requestOtp.username}")
                        val insertOtp = idUtente?.let { InsertOtp(otp = otp, idUtente = it) }
                        insertOtp?.let { insertOtp(it) }


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


    private fun generaOtp(): Int {
        val secureRandom = SecureRandom()
        val randomNumber = secureRandom.nextInt(900000) + 100000

        return randomNumber
    }


    private fun insertOtp(insertOtp: InsertOtp) {

        val query ="insert into otp (codice_otp, ref_utente) values (${insertOtp.otp}, ${insertOtp.idUtente})"
        Log.d("DEBUG", "La tua query sarà: ${query}")
        ClientNetwork.retrofit.inserOtp(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i(
                        "onResponse",
                        "Sono dentro la onResponseOTP e l'esito sarà: ${response.isSuccessful}"
                    )

                    if (response.isSuccessful) {

                        Log.i("Query", "Insert Riuscito")


                    } else {
                        val errorMessage = response.message()
                        Log.e("onResponse", "Errore Insert")



                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Recupero_Password_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }

}



