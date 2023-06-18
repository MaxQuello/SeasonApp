package com.example.seasonapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        // DATABASE NAME
        const val DB_NAME = "PWM_DB"

        // DATABASE VERSION
        const val DB_VERSION = 1

        //TABLE NAME
        const val TABLE_PERSONA = "PERSONA"

        const val USERNAME = "username"
        const val NOME = "nome"
        const val COGNOME = "cognome"
        const val TELEFONO = "telefono"
        const val EMAIL = "email"
        const val PASSWORD = "password"

        // STRING TO CREATE TABLE
        private const val SQL_CREATE_UTENTE =
            "CREATE TABLE $TABLE_PERSONA (" +
                    "$USERNAME TEXT PRIMARY KEY," +
                    "$NOME TEXT NOT NULL," +
                    "$COGNOME TEXT NOT NULL," +
                    "$TELEFONO TEXT," +
                    "$EMAIL TEXT NOT NULL," +
                    "$PASSWORD TEXT NOT NULL);"

        // STRING TO DROP TABLE
        private const val SQL_DELETE_UTENTE =
            "DROP TABLE IF EXISTS $TABLE_PERSONA;"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
        db?.execSQL(SQL_CREATE_UTENTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_UTENTE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}