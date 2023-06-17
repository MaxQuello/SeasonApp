package com.example.seasonapp

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
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.api.OnLoginStatusListener
import com.example.seasonapp.databinding.FragmentLoginBinding
import com.example.seasonapp.model.RequestLogin
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    var username = ""
    var password = ""
    private var loginStatusListener: OnLoginStatusListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginStatusListener) {
            loginStatusListener = context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        Log.d("SONO IO","Sono prima del bottone")
        binding.buttonLogin.setOnClickListener {
            if (binding.inserisciUsername.text.toString() != ""  && binding.inserisciPassword.text.toString() != ""){
                username = binding.inserisciUsername.text.toString()
                password = binding.inserisciPassword.text.toString()
                val loginRequestLogin = RequestLogin(username=username, password=password)
                Log.i("LOG-Login_Fragment", "chiamo la fun loginUtente passando: $loginRequestLogin ")
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

        val query = "select * from persona where username = '${requestLogin.username}' and password = '${requestLogin.password}';"
        Log.i("LOG-Login_Fragment", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        try{
                            getUser((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject)
                            Log.i("LOG-Login_Fragment-onResponse", "LOGGATO")
                            loginStatusListener?.onLoginSuccess()
                            if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                //Log.i("LOG-Login_Fragment-onResponse", "Sono dentro il secondo if. e chiamo la getImageProfilo")
                            } else {
                                Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE")
                                Toast.makeText(context,"credenziali errate", Toast.LENGTH_LONG).show()
                            }
                        }catch (e:Exception){
                            Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE")
                            Toast.makeText(context,"credenziali errate", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(context,"Inserisci le credenziali", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE")
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
        val username=jsonObject.get("username").asString
        val password=jsonObject.get("password").asString
        /*val cognome=jsonObject.get("cognome").asString
        val qr=jsonObject.get("qr").asString
        val type=jsonObject.get("type").asString*/
    }

}