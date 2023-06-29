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
        const val TABLE_UTENTE = "UTENTE"

        const val ID_UTENTE = "id_utente"
        const val ID_RISTORANTE = "id_ristorante"

        const val NOME = "nome"
        const val COGNOME = "cognome"
        const val GENDER = "gender"
        const val DATANASCITA = "data_nascita"
        const val TELEFONO = "telefono"
        const val EMAIL = "email"
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val RISPOSTA = "risposta"

        const val TABLE_RISTORANTE = "RISTORANTE"


        const val REF_UTENTE = "ref_utente"
        const val DATA_PRENOTAZIONE = "data_prenotazione"
        const val NUMERO_OSPITI = "numero_ospiti"
        const val CHOSENMEAL = "chosen_meal"

        const val TABLE_GYM = "GYM"
        const val ID_PRENOTAZIONE_GYM = "id_prenotazione_ristorante"
        const val REF_GYM = "ref_gym"
        const val DATA_GYM = "data_gym"
        const val OSPITI_GYM = "ospiti_gym"

        // STRING TO CREATE TABLE
        private const val SQL_CREATE_UTENTE =
            "CREATE TABLE $TABLE_UTENTE (" +
                    "$ID_UTENTE INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOME TEXT NOT NULL," +
                    "$COGNOME TEXT NOT NULL," +
                    "$GENDER TEXT NOT NULL," +
                    "$DATANASCITA TEXT NOT NULL,"+
                    "$EMAIL TEXT NOT NULL," +
                    "$TELEFONO TEXT," +
                    "$USERNAME TEXT NOT NULL UNIQUE,"+
                    "$PASSWORD TEXT NOT NULL," +
                    "$RISPOSTA TEXT NOT NULL);"

        private const val SQL_CREATE_RISTORANTE =
            "CREATE TABLE $TABLE_RISTORANTE (" +
                    "$ID_RISTORANTE INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$REF_UTENTE INTEGER," +
                    "$DATA_PRENOTAZIONE TEXT," +
                    "$NUMERO_OSPITI INTEGER," +
                    "$CHOSENMEAL TEXT," +
                    "FOREIGN KEY ($REF_UTENTE) REFERENCES $TABLE_UTENTE ($ID_UTENTE)" +
                    ");"

        private const val SQL_CREATE_GYM =
            "CREATE TABLE $TABLE_GYM (" +
                    "$ID_PRENOTAZIONE_GYM INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$REF_GYM INTEGER," +
                    "$DATA_GYM TEXT," +
                    "$OSPITI_GYM INTEGER," +
                    "FOREIGN KEY ($REF_GYM) REFERENCES $TABLE_UTENTE ($ID_UTENTE)" +
                    ");"




        // STRING TO DROP TABLE
        private const val SQL_DELETE_UTENTE =
            "DROP TABLE IF EXISTS $TABLE_UTENTE;"

        private const val SQL_DELETE_RISTORANTE =
            "DROP TABLE IF EXISTS $TABLE_RISTORANTE;"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
        db?.execSQL(SQL_CREATE_UTENTE)
        db?.execSQL(SQL_CREATE_RISTORANTE)
        db?.execSQL(SQL_CREATE_GYM)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_UTENTE)
        db?.execSQL(SQL_DELETE_RISTORANTE)
        db?.execSQL(SQL_CREATE_GYM)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}