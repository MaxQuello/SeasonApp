package com.example.seasonapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentGestisciServiziBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GestisciServiziFragment : Fragment() {
    private lateinit var binding : FragmentGestisciServiziBinding
    private var idUtente : Int? = null
    private lateinit var dbManager: DbManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGestisciServiziBinding.inflate(inflater)

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()


        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        if(checkiflogindone()){
            trovaprenotazionipalestra()
        }


        return binding.root
    }

    private fun trovaprenotazionipalestra() {
        val query = "SELECT * FROM prenotazioneGym WHERE id_utente = ${idUtente}"

        ClientNetwork.retrofit.getMyGymReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Log.d("PROVA","${response.body()}")
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Andata male",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Gestisci_Servizi-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun trovaprenotazioniImpianti() {
        val query = "SELECT * FROM prenotazioneImpianti WHERE id_utente = ${idUtente}"

        ClientNetwork.retrofit.getMyGymReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Log.d("PROVA","${response.body()}")
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Andata male",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Gestisci_Servizi-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun trovaprenotazioniRistorante() {
        val query = "SELECT * FROM prenotazioneRistorante WHERE id_utente = ${idUtente}"

        ClientNetwork.retrofit.getMyGymReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Log.d("PROVA","${response.body()}")
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Andata male",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Gestisci_Servizi-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun checkiflogindone(): Boolean {
        // Ottenere un'istanza delle SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Recuperare lo stato del login
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Verificare lo stato del login
        if (isLoggedIn) {
            Log.i("PROVA","LOGIN FATTO")
            return true
        } else {
            Log.i("PROVA","LOGIN NON FATTO")
            return false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbManager.close()
    }

}