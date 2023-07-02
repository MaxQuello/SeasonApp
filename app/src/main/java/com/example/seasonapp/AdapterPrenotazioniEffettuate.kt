package com.example.seasonapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seasonapp.databinding.CardViewPrenotazioniEffettuateBinding

class AdapterPrenotazioniEffettuate(private val data: ArrayList<PrenotazioniEffettuate>): RecyclerView.Adapter<AdapterPrenotazioniEffettuate.ViewHolder>() {
    class ViewHolder(val binding: CardViewPrenotazioniEffettuateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PrenotazioniEffettuate){
            binding.idPrenotazione.text = item.idPrenotazione.toString()
            binding.idUtentePren.text = item.idUtente.toString()
            binding.dataCheckIn2.text = item.dataCheckIn.toString()
            binding.dataCheckOut2.text = item.dataCheckOut.toString()
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