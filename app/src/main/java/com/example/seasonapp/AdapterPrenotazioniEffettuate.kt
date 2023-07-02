package com.example.seasonapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.databinding.CardViewPrenotazioniEffettuateBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class AdapterPrenotazioniEffettuate(private val data: ArrayList<PrenotazioniEffettuate>): RecyclerView.Adapter<AdapterPrenotazioniEffettuate.ViewHolder>() {
    class ViewHolder(val binding: CardViewPrenotazioniEffettuateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PrenotazioniEffettuate) {
            binding.idPrenotazione.text = item.idPrenotazione.toString()
            binding.idUtentePren.text = item.idUtente.toString()
            binding.dataCheckIn2.text = item.dataCheckIn.toString()
            binding.dataCheckOut2.text = item.dataCheckOut.toString()
            val currentDate: LocalDate = LocalDate.now()
            if (item.dataCheckIn!! < currentDate) {
                binding.buttonCancellaPrenotazione.visibility = View.GONE
            } else {
                binding.buttonCancellaPrenotazione.visibility = View.VISIBLE
            }
            binding.buttonCancellaPrenotazione.setOnClickListener {

                val query = "DELETE FROM reservations WHERE reservationId = ${item.idPrenotazione}"
                ClientNetwork.retrofit.remove(query).enqueue(
                    object : Callback<JsonObject> {
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            if (response.isSuccessful){
                                val context = binding.root.context
                                Toast.makeText(
                                    context,
                                    "Prenotazione cancellata",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
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
        val view = CardViewPrenotazioniEffettuateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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