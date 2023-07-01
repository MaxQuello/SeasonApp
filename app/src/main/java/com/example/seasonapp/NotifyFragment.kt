package com.example.seasonapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentNotifyBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotifyFragment : Fragment() {
    private lateinit var binding: FragmentNotifyBinding
    var idUtente : Int? = null
    private lateinit var dbManager: DbManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()


        idUtente = username?.let { dbManager.getUserIdByUsername(it) }



        prelevaNotifiche()

        binding = FragmentNotifyBinding.inflate(layoutInflater)
        return binding.root

    }

    private fun prelevaNotifiche() {
        val query = "select * from notifications WHERE ref_notification = ${idUtente}"

        ClientNetwork.retrofit.getNotifications(query).enqueue(
            object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        Log.d("NOTIFICHE","NOTIFICHE: ${response.body()}")
                    }else{
                        Log.d("PROBLEMA","PROBLEMA")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Servizi_Fragment-onFailure", "Errore ${t.message}")
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
}
