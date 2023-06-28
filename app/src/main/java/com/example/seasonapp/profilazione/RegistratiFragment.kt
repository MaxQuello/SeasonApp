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
import com.example.seasonapp.model.RequestRegistration
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    private lateinit var selectSpinnerDomanda: Spinner
    private lateinit var editTextRisposta : EditText
    private lateinit var registratiButton: Button
    private lateinit var dbManager: DbManager


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
        val testoRisposta = editTextRisposta.text.toString()


        if (testoNome.isNotEmpty() && testoCognome.isNotEmpty() && scelta.isNotEmpty() && testoDataNascita.isNotEmpty()
            && testoMail.isNotEmpty() && testoNumeroTelefono.isNotEmpty() && testoUsername.isNotEmpty()
            && testoPassword.isNotEmpty() && testoRisposta.isNotEmpty()){
            val requestRegistration = RequestRegistration(testoNome,testoCognome,scelta,testoDataNascita,testoMail,
            testoNumeroTelefono,testoUsername,testoPassword,testoRisposta)

            registraUtente(requestRegistration)
            /*dbManager.insertUtente(testoNome,testoCognome,scelta,testoDataNascita,testoMail,testoNumeroTelefono,testoUsername,
            testoPassword,testoRisposta)*/

        }else{
            Toast.makeText(
                requireContext(),
                "Completa i dati mancanti",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun registraUtente(requestRegistration: RequestRegistration) {

        val query = "INSERT INTO utente (nome, cognome, gender, dataNascita, mail, numeroDiTelefono, username, password, risposta) " +
                "VALUES ('${requestRegistration.nome}', '${requestRegistration.cognome}', '${requestRegistration.gender}'," +
                "'${requestRegistration.dataNascita}', '${requestRegistration.mail}', '${requestRegistration.numeroTelefono}'," +
                "'${requestRegistration.username}', '${requestRegistration.password}', '${requestRegistration.risposta}')"


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
                            dbManager.insertUtente(requestRegistration.nome,requestRegistration.cognome, requestRegistration.gender,
                                requestRegistration.dataNascita,requestRegistration.mail,requestRegistration.numeroTelefono,
                                requestRegistration.username,requestRegistration.password,requestRegistration.risposta)
                            val idUtente = dbManager.getUserIdByUsername("${requestRegistration.username}")
                            Log.d("ID UTENTE","L'id dell'utente è: ${idUtente}")
                            SessionManager.userId = idUtente
                            //DEVE PORTARE ALLA HOME
                            Toast.makeText(
                                requireContext(),
                                "Registrazione effettutata, fai l'accesso",
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


}