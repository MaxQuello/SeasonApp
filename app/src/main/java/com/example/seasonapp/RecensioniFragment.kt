package com.example.seasonapp

import AdapterRecensione
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentRecensioniBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecensioniFragment : Fragment() {
    private lateinit var binding: FragmentRecensioniBinding
    private lateinit var ratingBar: RatingBar
    var selectedStars: Float = 0f
    private lateinit var editTextRecensione : EditText
    private lateinit var buttonRecensisci : Button
    private var recensioneString :String? = null
    private lateinit var recensioni : ArrayList<Recensioni>

    private lateinit var dbManager: DbManager
    private var idUtente: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentRecensioniBinding.inflate(layoutInflater)

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()

        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        ratingBar= binding.ratingBarRecensioni
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            selectedStars = rating
        }

        editTextRecensione = binding.textScriviRecensione



        buttonRecensisci = binding.buttonRecensisci
        buttonRecensisci.setOnClickListener {
             recensioneString = editTextRecensione.text.toString()
            recensisci()
        }

        prelevaRecensioni()




        return binding.root

    }

    private fun recensisci() {
        val query = "INSERT INTO recensioni (ref_recensioni, numero_stelle, testo)" +
                "VALUES ($idUtente, $selectedStars, '$recensioneString');"
        Log.d("QUERY","QUERY: $query")

        ClientNetwork.retrofit.inserRecensione(query).enqueue(
            object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Toast.makeText(
                            requireContext(),
                            "Recensione aggiunta con successo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            "Recensione non aggiunta",
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

    private fun prelevaRecensioni(){
        val query = "SELECT * FROM recensioni"
        Log.d("QUERY","QUERY: $query")

        ClientNetwork.retrofit.getRecensioni(query).enqueue(object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    recensioni = ArrayList<Recensioni>()
                    val bodyString = response.body()
                    if (response.isSuccessful){
                        val jsonArray = response.body()?.getAsJsonArray("queryset")
                        val resultList = jsonArray?.mapNotNull { it as? JsonObject }
                        Log.d("RECENSIONI","RECENSIONI: ${response.body()}")
                        if (resultList != null) {
                            for(jsonObject in resultList){
                                val testo = jsonObject["testo"].toString()
                                val stelle = jsonObject["numero_stelle"].toString().toFloat()
                                recensioni.add(Recensioni(testo, stelle))
                            }

                        }
                        binding.listaRecensioni.adapter = AdapterRecensione(recensioni)
                        binding.listaRecensioni.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    }else{
                        Log.d("PROBLEMA","PROBLEMA")
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