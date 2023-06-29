package com.example.seasonapp.profilazione

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentLoginBinding
import com.example.seasonapp.model.RequestLogin
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var idRicevuto : Int? = null
    var username = ""
    var password = ""
    private lateinit var dbManager: DbManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        dbManager = DbManager(requireContext())
        dbManager.open()  //apertura connessione al Database locale -->LocalDB

        Log.d("SONO IO","Sono prima del bottone")
        binding.buttonLogin.setOnClickListener {
            if (binding.inserisciUsername.text.toString() != ""  && binding.inserisciPassword.text.toString() != ""){
                username = binding.inserisciUsername.text.toString()
                password = binding.inserisciPassword.text.toString()
                val loginRequestLogin = RequestLogin(username=username, password=password)
                Log.i("LOG-Login_Fragment", "chiamo la fun loginUtente passando: $loginRequestLogin ")
                getIdUtente(loginRequestLogin)
                loginUtente(loginRequestLogin)
            }else{
                Log.i("LOG-Login_Fragment", "L'utente non ha inserito le credenziali")
                Toast.makeText(context,"Inserisci le credenziali", Toast.LENGTH_LONG).show()
            }

        }

        val indietro = view.findViewById<ImageButton>(R.id.buttonIndietroLogin)
        indietro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }

        val recuperopass = view.findViewById<TextView>(R.id.recuperoPassword)
        recuperopass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperoPasswordFragment)
        }

        val recuperouser = view.findViewById<TextView>(R.id.recuperoUsername)
        recuperouser.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recuperoUsernameFragment)
        }


        return view

    }

    private fun loginUtente (requestLogin: RequestLogin){

        val query = "select * from utente where username = '${requestLogin.username}' and password = '${requestLogin.password}';"
        Log.i("LOG-Login_Fragment", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        try{
                            getUser((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject)
                            Log.i("LOG-Login_Fragment-onResponse", "LOGGATO")
                            val idUtente = dbManager.getUserIdByUsername(requestLogin.username)
                            val username = requestLogin.username
                            val sessionManager = SessionManager.getInstance(requireContext())
                            sessionManager.setUsername(username)
                            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

                            // Ottenere un'istanza dell'editor delle SharedPreferences
                            val editor = sharedPreferences.edit()

                            // Impostare lo stato del login su true
                            editor.putBoolean("isLoggedIn", true)

                            // Applicare le modifiche
                            editor.apply()

                            if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                //Log.i("LOG-Login_Fragment-onResponse", "Sono dentro il secondo if. e chiamo la getImageProfilo")
                            } else {
                                Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE1")
                                Toast.makeText(context,"credenziali errate", Toast.LENGTH_LONG).show()
                            }
                        }catch (e:Exception){
                            Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE2")
                            Toast.makeText(context,"credenziali errate", Toast.LENGTH_LONG).show()
                            Log.e("LOG-Login_Fragment-onResponse", "Eccezione:", e)
                        }

                    }else{
                        Toast.makeText(context,"Inserisci le credenziali", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE3")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun getUser(jsonObject: JsonObject){
        val id = jsonObject.get("id").asInt
        val nome = jsonObject.get("nome").asString
        val cognome=jsonObject.get("cognome").asString
        val gender = jsonObject.get("gender").asString
        val dataNascita = jsonObject.get("dataNascita").asString
        val email=jsonObject.get("mail").asString
        val telefono = jsonObject.get("numeroDiTelefono").asString
        val username=jsonObject.get("username").asString
        val password=jsonObject.get("password").asString
        val risposta = jsonObject.get("risposta").asString
        dbManager.insertUtente(id,nome,cognome,gender,dataNascita,email,telefono,username,password,risposta)
    }

    private fun getIdUtente(requestLogin: RequestLogin) {
        val query = "select id from utente where username = '${requestLogin.username}' and password = '${requestLogin.password}';"
        ClientNetwork.retrofit.getIdUtente(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        val jsonObject = response.body()
                        val jsonArray = jsonObject?.getAsJsonArray("queryset")
                        if (jsonArray != null && jsonArray.size() > 0) {
                            val firstObject = jsonArray.get(0).asJsonObject
                            val idUtente: Int? = firstObject.getAsJsonPrimitive("id")?.asInt
                            Log.d("Prova1", "L'id è: ${idUtente}")
                        }
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