package com.example.seasonapp.prenotazioneCamere

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
import com.example.seasonapp.databinding.FragmentGestisciPrenotazioniBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GestisciPrenotazioniFragment : Fragment() {
    private lateinit var binding: FragmentGestisciPrenotazioniBinding
    private var idUtente : Int? = null
    private lateinit var dbManager: DbManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGestisciPrenotazioniBinding.inflate(inflater)
        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()


        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        if (checkiflogindone()){
            trovaprenotazioni()
        }else{
            Toast.makeText(
                requireContext(),
                "Devi prima effettuare il login per vedere le tue prenotazioni",
                Toast.LENGTH_LONG
            ).show()
        }



        return binding.root
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

    private fun trovaprenotazioni() {
        val query = "SELECT * FROM reservations WHERE ref_reservations = ${idUtente}"

        ClientNetwork.retrofit.getMyReservations(query).enqueue(
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
                    Log.i("LOG-Prenota_Fragmemt-onFailure", "Errore accesso ${t.message}")
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