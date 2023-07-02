package com.example.seasonapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.databinding.CardViewGympassBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class AdapterGym(private val data: ArrayList<Gym>): RecyclerView.Adapter<AdapterGym.ViewHolder>() {
    class ViewHolder(val binding: CardViewGympassBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Gym){
            binding.idPrenotazioneGym.text = item.id_prenotazione.toString()
            binding.nOspitiGym.text = item.nOspiti.toString()
            binding.dataPrenotazioneGym.text = item.data_prenotazione.toString()
            val currentDate: LocalDate = LocalDate.now()
            if (item.data_prenotazione!! < currentDate) {
                binding.buttonCancellaGym.visibility = View.GONE
            } else {
                binding.buttonCancellaGym.visibility = View.VISIBLE
            }
            binding.buttonCancellaGym.setOnClickListener {

                val query = "DELETE FROM prenotazioneGym WHERE id = ${item.id_prenotazione}"
                ClientNetwork.retrofit.remove(query).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if (response.isSuccessful) {
                                val context = binding.root.context
                                Toast.makeText(
                                    context,
                                    "Prenotazione cancellata",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val context = binding.root.context
                                Toast.makeText(
                                    context,
                                    "Prenotazione non cancellata",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            Log.i("LOG-adapter-onFailure", "Errore ${t.message}")
                            val context = binding.root.context
                            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                )


            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewGympassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
}