package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentVoucherBinding


class VoucherFragment : Fragment() {
    private lateinit var binding: FragmentVoucherBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentVoucherBinding.inflate(layoutInflater)
        return binding.root

    }
}