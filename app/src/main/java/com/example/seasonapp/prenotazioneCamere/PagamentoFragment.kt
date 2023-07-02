package com.example.seasonapp.prenotazioneCamere

import AdapterOfferte
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seasonapp.Offerta
import com.example.seasonapp.databinding.FragmentPagamentoBinding


class PagamentoFragment : Fragment() {
    private lateinit var binding: FragmentPagamentoBinding
    private var listaOfferte: ArrayList<Offerta>? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            listaOfferte = @Suppress("DEPRECATION")
            arguments?.getParcelableArrayList<Offerta>("offerte")?.let {
                ArrayList(it)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentPagamentoBinding.inflate(layoutInflater)
        binding.listaOffertePagamento.adapter = listaOfferte?.let { AdapterOfferte(it) }
        binding.listaOffertePagamento.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

}