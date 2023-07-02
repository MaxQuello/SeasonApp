package com.example.seasonapp.profilazione

import android.R
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentRegistratiBinding
import com.example.seasonapp.model.RequestLogin
import com.example.seasonapp.model.RequestRegistration
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.SecureRandom


class RegistratiFragment : Fragment() {
    private lateinit var binding: FragmentRegistratiBinding
    private lateinit var multiSelectSpinner: Spinner
    private lateinit var editTextNome : EditText
    private lateinit var editTextCognome : EditText
    private lateinit var radioGroupGender : RadioGroup
    private lateinit var editTextDataNascita : EditText
    private lateinit var editTextMail: EditText
    private lateinit var editTextNumeroDiTelefono : EditText
    private lateinit var editTextUsername : EditText
    private lateinit var editTextPassword : EditText
    private lateinit var editTextRisposta : EditText
    private lateinit var registratiButton: Button
    private lateinit var dbManager: DbManager
    private var codiceSconto : Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentRegistratiBinding.inflate(layoutInflater)
        multiSelectSpinner = binding.multiSelectSpinner
        val options = arrayOf("Cognome da nubile di tua madre", "La tua squadra del cuore", "Nome del tuo animale domestico")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        multiSelectSpinner.adapter = adapter

        val buttonIndietro = binding.buttonIndietroRegistrati
        buttonIndietro.setOnClickListener {
            findNavController().navigate(com.example.seasonapp.R.id.action_registratiFragment_to_profileFragment)

        }

        dbManager = DbManager(requireContext())
        dbManager.open()


        editTextNome = binding.inserisciNome
        editTextCognome = binding.inserisciCognome
        radioGroupGender = binding.radioGroupGender
        editTextDataNascita = binding.inserisciDataNascita
        editTextMail = binding.inserisciEmail
        editTextNumeroDiTelefono = binding.inserisciNumero
        editTextUsername = binding.inserisciUsername
        editTextPassword = binding.inserisciPassword
        editTextRisposta = binding.inserisciRispostaSicurezza

        registratiButton = binding.buttonRegistrati


        registratiButton.setOnClickListener {
            registration()

        }
        return binding.root
    }

    private fun registration() {
        val testoNome = editTextNome.text.toString()
        val testoCognome = editTextCognome.text.toString()
        val radioButtonId = radioGroupGender.checkedRadioButtonId
        val radioButton = view?.findViewById<RadioButton>(radioButtonId)
        val scelta = radioButton?.text.toString()
        val testoDataNascita = editTextDataNascita.text.toString()
        val testoMail = editTextMail.text.toString()
        val testoNumeroTelefono = editTextNumeroDiTelefono.text.toString()
        val testoUsername = editTextUsername.text.toString()
        val testoPassword = editTextPassword.text.toString()
        val testoDomanda = multiSelectSpinner.selectedItem.toString()
        val testoRisposta = editTextRisposta.text.toString()

        if (testoNome.isNotEmpty() && testoCognome.isNotEmpty() && scelta.isNotEmpty() && testoDataNascita.isNotEmpty()
            && testoMail.isNotEmpty() && testoNumeroTelefono.isNotEmpty() && testoUsername.isNotEmpty()
            && testoPassword.isNotEmpty() && testoDomanda.isNotEmpty() && testoRisposta.isNotEmpty()) {

            codiceSconto = generaCodiceSconto()

            val requestRegistration = RequestRegistration(testoNome, testoCognome, scelta, testoDataNascita, testoMail,
                testoNumeroTelefono, testoUsername, testoPassword, testoDomanda, testoRisposta,
                codiceSconto!!
            )

            registraUtente(requestRegistration) { idUtente ->
                if (idUtente != null) {
                    dbManager.insertUtente(
                        idUtente,
                        testoNome,
                        testoCognome,
                        scelta,
                        testoDataNascita,
                        testoMail,
                        testoNumeroTelefono,
                        testoUsername,
                        testoPassword,
                        testoDomanda,
                        testoRisposta,
                        codiceSconto!!
                    )
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Completa i dati mancanti",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun generaCodiceSconto() : Int {
        val secureRandom = SecureRandom()
        val randomNumber = secureRandom.nextInt(900000) + 100000

        return randomNumber
    }

    private fun registraUtente(requestRegistration: RequestRegistration, callback: (idUtente: Int?) -> Unit) {

        val query = "INSERT INTO utente (nome, cognome, gender, dataNascita, mail, numeroDiTelefono, username, password, domanda, risposta,codice_sconto) " +
                "VALUES ('${requestRegistration.nome}', '${requestRegistration.cognome}', '${requestRegistration.gender}'," +
                "'${requestRegistration.dataNascita}', '${requestRegistration.mail}', '${requestRegistration.numeroTelefono}'," +
                "'${requestRegistration.username}', '${requestRegistration.password}', '${requestRegistration.domanda}', '${requestRegistration.risposta}'," +
                "'${requestRegistration.codiceSconto}')"


        Log.d("DEBUG","La tua query sarà: ${query}")

        ClientNetwork.retrofit.insertUser(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    val bodyString = response.body()
                    Log.i("onResponse", "Sono dentro la onResponse e il body sara : ${bodyString}")
                    if (response.isSuccessful) {
                        Log.d("ONRESPONSE","REGISTRATO")
                        try {
                            getIdUtente(requestRegistration) { idUtente ->
                                if (idUtente != null) {
                                    codiceSconto?.let {
                                        dbManager.insertUtente(
                                            idUtente,
                                            requestRegistration.nome,
                                            requestRegistration.cognome,
                                            requestRegistration.gender,
                                            requestRegistration.dataNascita,
                                            requestRegistration.mail,
                                            requestRegistration.numeroTelefono,
                                            requestRegistration.username,
                                            requestRegistration.password,
                                            requestRegistration.domanda,
                                            requestRegistration.risposta,
                                            it
                                        )
                                    }
                                }
                            }
                            findNavController().navigate(com.example.seasonapp.R.id.action_registratiFragment_to_homeFragment2)
                            Toast.makeText(
                                requireContext(),
                                "Registrazione effettuata, fai l'accesso",
                                Toast.LENGTH_LONG
                            ).show()
                        }catch (e: SQLiteConstraintException){
                            Toast.makeText(
                                requireContext(),
                            "L'username inserito è gia esistente",
                            Toast.LENGTH_SHORT).show()
                        }


                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Username gia registrato",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Resturant_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )

    }

    private fun getIdUtente(requestRegistration: RequestRegistration,callback: (idUtente: Int?) -> Unit) {
        val query = "select id from utente where username = '${requestRegistration.username}' and password = '${requestRegistration.password}';"
        ClientNetwork.retrofit.getIdUtente(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val jsonArray = jsonObject?.getAsJsonArray("queryset")
                        if (jsonArray != null && jsonArray.size() > 0) {
                            val firstObject = jsonArray.get(0).asJsonObject
                            val idUtente: Int? = firstObject.getAsJsonPrimitive("id")?.asInt
                            //SessionManager.userId = idUtente

                            Log.d("Prova1", "L'id è: ${idUtente}")
                            callback(idUtente)
                        } else {
                            // Nessun risultato trovato
                            callback(null)
                        }
                    } else {
                        // Errore nella chiamata API
                        callback(null)
                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "L'id utente non esiste",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )

    }


}