package com.example.seasonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.databinding.CardViewImpiantoBinding

class AdapterImpianto(private val data: ArrayList<Impianto>): RecyclerView.Adapter<AdapterImpianto.ViewHolder>() {
    class ViewHolder(val binding: CardViewImpiantoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Impianto){
            binding.idPrenotazioneImpianto.text = item.id_prenotazione.toString()
            binding.idUtenteImpianto.text = item.ref_utente.toString()
            binding.dataPrenotazioneImpianto.text = item.data_prenotazione.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewImpiantoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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