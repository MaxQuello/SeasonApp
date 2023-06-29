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
}
