package com.example.seasonapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentGestisciServiziBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GestisciServiziFragment : Fragment() {
    private lateinit var binding : FragmentGestisciServiziBinding
    private var idUtente : Int? = null
    private lateinit var dbManager: DbManager
    private lateinit var palestra : ArrayList<Gym>
    private lateinit var ristorante : ArrayList<Ristorante>
    private lateinit var impianto : ArrayList<Impianto>

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
            trovaprenotazioniImpianti()
            trovaprenotazioniRistorante()
        }


        return binding.root
    }

    private fun trovaprenotazionipalestra() {
        val query = "SELECT * FROM prenotazioneGym WHERE id_utente = ${idUtente}"

        ClientNetwork.retrofit.getMyGymReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val bodyString = response.body()
                    if (response.isSuccessful){
                        palestra = ArrayList<Gym>()
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        Log.d("QUERY","risultati:  ${jsonArray}")
                        val resultList = jsonArray?.mapNotNull { it as? JsonObject }
                        Log.d("listarisultati","lista risultati:  ${resultList}")
                        if (resultList != null) {
                            for (jsonObject in resultList) {
                                val id_prenotazione = jsonObject["id"].toString().toInt()
                                val nOspiti = jsonObject["numero_ospiti"].toString().toInt()
                                val dataPrenotazione =
                                    jsonObject["data_prenotazione"].toString().replace("\"", "")
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val dateRes = LocalDate.parse(dataPrenotazione, formatter)
                                palestra.add(Gym(id_prenotazione, dateRes, nOspiti))
                            }
                        }
                        binding.listaGym.adapter = AdapterGym(palestra)
                        binding.listaGym.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
        val query = "SELECT * FROM prenotazioneImpianti WHERE ref_impianti = ${idUtente}"

        ClientNetwork.retrofit.getMyImpiantiReservation(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val bodyString = response.body()
                    if (response.isSuccessful){
                        impianto = ArrayList<Impianto>()
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        val resultList = jsonArray?.mapNotNull { it as? JsonObject }
                        Log.d("listarisultati","lista risultati:  ${resultList}")
                        if (resultList != null) {
                            for (jsonObject in resultList) {
                                val id_prenotazione = jsonObject["id_prenotazione_impianto"].toString().toInt()
                                val id_utente = jsonObject["ref_impianti"].toString().toInt()
                                val dataPrenotazione =
                                    jsonObject["data_prenotazione_impianti"].toString().replace("\"", "")
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val dateRes = LocalDate.parse(dataPrenotazione, formatter)
                                impianto.add(Impianto(id_prenotazione, dateRes, id_utente))
                            }
                        }
                        binding.listaImpianto.adapter = AdapterImpianto(impianto)
                        binding.listaImpianto.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Query Impianti Error",
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
                    val bodyString = response.body()
                    if (response.isSuccessful){
                        ristorante = ArrayList<Ristorante>()
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        Log.d("QUERY","risultati:  ${jsonArray}")
                        val resultList = jsonArray?.mapNotNull { it as? JsonObject }
                        Log.d("listarisultati","lista risultati:  ${resultList}")
                        if (resultList != null) {
                            for (jsonObject in resultList) {
                                val id_prenotazione = jsonObject["id"].toString().toInt()
                                val nOspiti = jsonObject["numero_ospiti"].toString().toInt()
                                val orario = jsonObject["chosen_meal"].toString()
                                val dataPrenotazione =
                                    jsonObject["data_prenotazione"].toString().replace("\"", "")
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val dateRes = LocalDate.parse(dataPrenotazione, formatter)
                                ristorante.add(Ristorante(id_prenotazione, dateRes, nOspiti, orario))
                            }
                        }
                        binding.listaRistorante.adapter = AdapterRistorante(ristorante)
                        binding.listaRistorante.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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