package com.example.seasonapp.profilazione

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentModificaPasswordBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModificaPasswordFragment : Fragment() {
    private lateinit var binding: FragmentModificaPasswordBinding
    private lateinit var passwordTextView: TextView
    private lateinit var ripetiPasswordTextView: TextView
    private lateinit var modificaButton : Button
    private var passwordString : String? = null
    private var ripetiPasswordString : String? = null
    val sessionManager = context?.let { SessionManager.getInstance(it) }
    var username : String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentModificaPasswordBinding.inflate(layoutInflater)

        passwordTextView = binding.inserisciPassword
        ripetiPasswordTextView = binding.reinserisciPassword
        modificaButton = binding.buttonModificaPassword

        modificaButton.setOnClickListener {
            username= sessionManager?.getUsername()
            Log.d("USERNAME","USERNAME : $username")
            passwordString= passwordTextView.text.toString()
            ripetiPasswordString = ripetiPasswordTextView.text.toString()
            if (passwordString == ripetiPasswordString){
                modificaPassword()
            }else{
                Toast.makeText(
                    requireContext(),
                    "Password non corrispondenti",
                    Toast.LENGTH_LONG
                ).show()
            }

        }



        return binding.root

    }

    private fun modificaPassword() {
        val query = "UPDATE utente" +
                 "password = '$passwordString'" +
                " WHERE username = $username"

        Log.d("QUERY","QUERY: $query")

        ClientNetwork.retrofit.modifica(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Toast.makeText(
                            requireContext(),
                            "La tua nuova password è >$passwordString",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Password non modificata",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
}