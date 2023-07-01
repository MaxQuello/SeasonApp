package com.example.seasonapp.profilazione


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.seasonapp.R
import com.example.seasonapp.api.ClientNetwork
import com.example.seasonapp.data.DbManager
import com.example.seasonapp.data.SessionManager
import com.example.seasonapp.databinding.FragmentModificaProfiloBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ModificaProfiloFragment : Fragment() {
    private lateinit var binding: FragmentModificaProfiloBinding
    private lateinit var dbManager: DbManager
    private var idUtente: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificaProfiloBinding.inflate(inflater, container, false)
        val view = binding.root

        // ...

        dbManager = DbManager(requireContext())
        dbManager.open()

        val sessionManager = SessionManager.getInstance(requireContext())
        val username = sessionManager.getUsername()

        idUtente = username?.let { dbManager.getUserIdByUsername(it) }

        val coroutineScope = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch {
            val datiUtente = getDatiUtente()

            if (datiUtente != null) {
                val nomeUtente = datiUtente.get("nome")?.asString ?: ""
                val cognomeUtente = datiUtente.get("cognome")?.asString ?: ""
                val dataNascitaUtente = datiUtente.get("dataNascita")?.asString ?: ""

                // Aggiorna la TextView con i dati dell'utente
                val nomeText = nomeUtente ?: ""
                val nomeUtenteString = getString(R.string.nomeUtenteLoggato, nomeText)
                binding.textNome.text = nomeUtenteString

                val cognomeText = cognomeUtente ?: ""
                val cognomeUtenteString = getString(R.string.cognomeUtenteLoggato, cognomeText)
                binding.textCognome.text = cognomeUtenteString

                val dataNascita = dataNascitaUtente ?: ""
                val dataUtente = getString(R.string.dataNascitaUtenteLoggato, dataNascita)
                binding.textDataNascita.text = dataUtente

                val ediTextMail = binding.inserisciEmail
                ediTextMail.setText(datiUtente.get("mail")?.asString)

                val editTextNumero = binding.inserisciNumero
                editTextNumero.setText(datiUtente.get("numeroDiTelefono")?.asString)

                val editTextUsername = binding.inserisciUsername
                editTextUsername.setText(datiUtente.get("username")?.asString)

                val editTextPassword = binding.inserisciPassword
                editTextPassword.setText(datiUtente.get("password")?.asString)

                val buttonModifica = binding.buttonModifica
                buttonModifica.setOnClickListener {
                    val testoMail = ediTextMail.text.toString()
                    val testoNumero = editTextNumero.text.toString()
                    val testoUsername = editTextUsername.text.toString()
                    val testoPassword = editTextPassword.text.toString()

                    updateDati(testoMail, testoNumero, testoUsername, testoPassword)
                }
            }
        }

        return view
    }

    private suspend fun getDatiUtente(): JsonObject? {
        val query = "SELECT * FROM utente WHERE id=${idUtente}"
        Log.d("query", "LA QUERY E' $query")

        return suspendCancellableCoroutine { continuation ->
            ClientNetwork.retrofit.getUser(query).enqueue(
                object : Callback<JsonObject> {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful) {
                            val jsonObject = response.body()?.getAsJsonArray("queryset")?.get(0)?.asJsonObject
                            continuation.resume(jsonObject)
                        } else {
                            continuation.resume(null)
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        continuation.resume(null)
                    }
                }
            )

            continuation.invokeOnCancellation {
                // Annulla la chiamata API se la coroutine viene cancellata
                // TODO: Annulla la chiamata API se necessario
            }
        }
    }

    private fun updateDati(mail: String, numero: String, username: String, password: String) {
        val query = "UPDATE utente" +
                " SET mail = '$mail', numeroDiTelefono = '$numero', username = '$username'," +
                " password = '$password'" +
                " WHERE id = $idUtente"

        Log.d("QUERY", "QUERY: $query")

        ClientNetwork.retrofit.modifica(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Modifica avvenuta con successo",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_modificaProfiloFragment_to_profileFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Non è stato possibile modificare i dati inseriti",
                            Toast.LENGTH_LONG
                        ).show()
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



