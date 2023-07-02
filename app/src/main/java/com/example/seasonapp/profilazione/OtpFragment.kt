package com.example.seasonapp.profilazione

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.databinding.FragmentOtpBinding
import com.example.seasonapp.databinding.FragmentPrenotaBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpFragment : Fragment() {
    private lateinit var binding : FragmentOtpBinding
    private lateinit var buttonContinua : Button
    private lateinit var buttonIndietro : Button
    private lateinit var editTextOtp : EditText
    private var editTextOtpString : Int? = null
    private var codiceOtp: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentOtpBinding.inflate(inflater)


        val buttonIndietro = view?.findViewById<ImageButton>(R.id.buttonIndietro3)
        buttonIndietro?.setOnClickListener {
            findNavController().navigate(R.id.action_otpFragment_to_recuperoPasswordFragment)

        }


        buttonContinua = binding.buttonContinua
        buttonContinua.setOnClickListener {
            editTextOtp = binding.otp
            editTextOtpString= editTextOtp.text.toString().toInt()
            verificaOtp()


        }
        return binding.root
    }

    private fun verificaOtp() {
        val query = "SELECT codice_otp from otp WHERE codice_otp = ${editTextOtpString}"
        ClientNetwork.retrofit.otp(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val codiceOtpJsonArray = responseBody?.getAsJsonArray("queryset")

                        if (codiceOtpJsonArray != null && codiceOtpJsonArray.size() > 0) {
                            val codiceOtpJson = codiceOtpJsonArray[0].asJsonObject
                                .get("codice_otp")

                            codiceOtp = codiceOtpJson?.asInt ?: 0

                            if (codiceOtp == editTextOtpString) {
                                findNavController().navigate(R.id.action_otpFragment_to_modificaPasswordFragment)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Codice otp sbagliato, riprovare",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            // L'array "queryset" è vuoto, gestisci il caso appropriato
                            Toast.makeText(
                                requireContext(),
                                "Codice otp sbagliato, riprovare",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
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