package com.example.seasonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.databinding.CardViewRistoranteBinding

class AdapterRistorante(private val data: ArrayList<Ristorante>): RecyclerView.Adapter<AdapterRistorante.ViewHolder>() {
    class ViewHolder(val binding: CardViewRistoranteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Ristorante){
            binding.dataPrenotazioneRistorante.text = item.data_prenotazione.toString()
            binding.nOspitiRistorante.text = item.nOspiti.toString()
            binding.idPrenotazioneRistorante.text = item.id_prenotazione.toString()
            binding.pranzoCena.text = item.orario

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewRistoranteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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