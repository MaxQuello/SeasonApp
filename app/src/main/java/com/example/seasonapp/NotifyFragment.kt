package com.example.seasonapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.databinding.FragmentNotifyBinding


class NotifyFragment : Fragment() {
    private lateinit var binding: FragmentNotifyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()

        binding = FragmentNotifyBinding.inflate(layoutInflater)
        return binding.root

    }
}
