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
        const val ID_UTENTE =  "id_utente"
        const val NOME = "nome"
        const val COGNOME = "cognome"
        const val GENDER = "gender"
        const val DATANASCITA = "data_nascita"
        const val TELEFONO = "telefono"
        const val EMAIL = "email"
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val DOMANDA = "domanda"
        const val RISPOSTA = "risposta"

        const val TABLE_RISTORANTE = "RISTORANTE"
        const val ID_RISTORANTE = "id_ristorante"
        const val REF_UTENTE = "ref_utente"
        const val DATA_PRENOTAZIONE = "data_prenotazione"
        const val NUMERO_OSPITI = "numero_ospiti"
        const val CHOSENMEAL = "chosen_meal"

        const val TABLE_GYM = "GYM"
        const val ID_PRENOTAZIONE_GYM = "id_prenotazione_ristorante"
        const val REF_GYM = "ref_gym"
        const val DATA_GYM = "data_gym"
        const val OSPITI_GYM = "ospiti_gym"

        const val TABLE_RESERVATIONS = "RESERVATIONS"
        const val ID_RESERVATIONS = "id_reservations"
        const val ROOMID = "room_id"
        const val CHECKINDATE = "check_in_date"
        const val CHECKOUTDATE = "check_out_date"
        const val REFRESERVATIONS = "ref_reservations"

        const val TABLE_ROOMS = "ROOMS"
        const val ROOMIDROOMS = "room_id_rooms"
        const val ROOM_TYPE = "room_type"
        const val CAPACITY = "capacity"
        const val AVAILABILITY = "avability3"

        const val TABLE_OTP = "OTP"
        const val CODICE_OTP = "codice_otp"
        const val REFOTP = "ref_otp"

        const val TABLE_IMPIANTI = "impianti"
        const val IDPRENOTAZIONEIMPIANTO = "id_prenotazione_impianto"
        const val REFIMPIANTO = "ref_impianto"
        const val DATAIMPIANTO = "data_impianto"
//



        // STRING TO CREATE TABLE
        private const val SQL_CREATE_UTENTE =
            "CREATE TABLE $TABLE_UTENTE (" +
                    "$ID_UTENTE INTEGER PRIMARY KEY," +
                    "$NOME TEXT NOT NULL," +
                    "$COGNOME TEXT NOT NULL," +
                    "$GENDER TEXT NOT NULL," +
                    "$DATANASCITA TEXT NOT NULL,"+
                    "$EMAIL TEXT NOT NULL," +
                    "$TELEFONO TEXT," +
                    "$USERNAME TEXT NOT NULL UNIQUE,"+
                    "$PASSWORD TEXT NOT NULL," +
                    "$DOMANDA TEXT NOT NULL," +
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

        private const val SQL_CREATE_RESERVATIONS =
            "CREATE TABLE $TABLE_RESERVATIONS (" +
                    "$ID_RESERVATIONS INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$ROOMID INTEGER," +
                    "$CHECKINDATE TEXT," +
                    "$CHECKOUTDATE TEXT," +
                    "$REFRESERVATIONS INTEGER," +
                    "FOREIGN KEY ($ROOMID) REFERENCES $TABLE_ROOMS ($ROOMIDROOMS)," +
                    "FOREIGN KEY ($REFRESERVATIONS) REFERENCES $TABLE_UTENTE ($ID_UTENTE)" +
                    ");"

        private const val SQL_CREATE_ROOMS =
            "CREATE TABLE $TABLE_ROOMS (" +
                    "$ROOMIDROOMS INTEGER PRIMARY KEY," +
                    "$ROOM_TYPE TEXT," +
                    "$CAPACITY INTEGER," +
                    "$AVAILABILITY INTEGER" +
                    ");"

        private const val SQL_CREATE_OTP =
            "CREATE TABLE $TABLE_OTP (" +
                    "$CODICE_OTP INTEGER PRIMARY KEY," +
                    "$REFOTP INTEGER," +
                    "FOREIGN KEY ($REFOTP) REFERENCES $TABLE_UTENTE ($ID_UTENTE)" +
                    ");"

        private const val SQL_CREATE_IMPIANTI =
            "CREATE TABLE $TABLE_IMPIANTI (" +
                    "$IDPRENOTAZIONEIMPIANTO INTEGER PRIMARY KEY," +
                    "$REFIMPIANTO INTEGER," +
                    "$DATAIMPIANTO TEXT," +
                    "FOREIGN KEY ($REFIMPIANTO) REFERENCES $TABLE_UTENTE ($ID_UTENTE)" +
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
        db?.execSQL(SQL_CREATE_ROOMS)
        db?.execSQL(SQL_CREATE_RESERVATIONS)
        db?.execSQL(SQL_CREATE_OTP)
        db?.execSQL(SQL_CREATE_IMPIANTI)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_UTENTE)
        db?.execSQL(SQL_DELETE_RISTORANTE)
        db?.execSQL(SQL_CREATE_GYM)
        db?.execSQL(SQL_CREATE_ROOMS)
        db?.execSQL(SQL_CREATE_RESERVATIONS)
        db?.execSQL(SQL_CREATE_OTP)
        db?.execSQL(SQL_CREATE_IMPIANTI)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}