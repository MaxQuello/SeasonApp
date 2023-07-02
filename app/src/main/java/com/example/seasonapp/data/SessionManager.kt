package com.example.seasonapp.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager private constructor(context: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val PREF_NAME = "AppSession"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_ID = "id"

        @Volatile
        private var instance: SessionManager? = null

        fun getInstance(context: Context): SessionManager =
            instance ?: synchronized(this) {
                instance ?: SessionManager(context).also { instance = it }
            }
    }

    fun setUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun setEmail(email: String) {
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }
    fun setId(id: Int?) {
        sharedPreferences.edit().putInt(KEY_ID, id ?: -1).apply()
    }

    fun getId(): Int? {
        val id = sharedPreferences.getInt(KEY_ID, -1)
        return if (id != -1) id else null
    }


}
