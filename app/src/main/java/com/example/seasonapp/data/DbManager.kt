package com.example.seasonapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.time.LocalDate

class DbManager(val context: Context) {
    private lateinit var helper: DBHelper
    private lateinit var db: SQLiteDatabase

    fun open(): DbManager{
        helper = DBHelper(context)
        db = helper.writableDatabase
        return this
    }

    fun close(){
        helper.close()
    }

    fun insertUtente(
        nome: String,
        cognome: String,
        gender: String,
        dataNascita: String,
        email: String,
        telefono: String,
        username: String,
        password: String,
        risposta: String
    ) {
        val value = ContentValues().apply {
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.GENDER, gender)
            put(DBHelper.EMAIL, email)
            put(DBHelper.DATANASCITA, dataNascita)
            put(DBHelper.EMAIL, email)
            put(DBHelper.TELEFONO, telefono)
            put(DBHelper.USERNAME, username)
            put(DBHelper.PASSWORD, password)
            put(DBHelper.RISPOSTA, risposta)
        }
        db.insertWithOnConflict(DBHelper.TABLE_UTENTE, null, value, SQLiteDatabase.CONFLICT_IGNORE)
    }


    fun updateUtente(idUtente: Int, nome: String, cognome: String, telefono: String, email: String): Int{
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())
        val values = ContentValues().apply {
            put(DBHelper.USERNAME, idUtente)
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.TELEFONO, telefono)
            put(DBHelper.EMAIL, email)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updateTelefono(idUtente: Int, telefono: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.TELEFONO, telefono)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updateEmail(idUtente: Int, email: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.EMAIL, email)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updatePassword(idUtente: Int, password: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.PASSWORD, password)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun deleteUtente(idUtente: Int){
        val selection = "${DBHelper.USERNAME} = ?"
        val selectionArgs = arrayOf(idUtente.toString())
        db.delete(DBHelper.TABLE_UTENTE, selection, selectionArgs)
    }

    fun deleteAll(){
        db.delete(DBHelper.TABLE_UTENTE, null, null)
    }

    fun selectUtente(): Cursor{
        val projection = arrayOf(DBHelper.USERNAME, DBHelper.EMAIL, DBHelper.PASSWORD)
        val cursor = db.query(
            DBHelper.TABLE_UTENTE,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun selectDatiUtente(): Cursor{
        val projection = arrayOf(DBHelper.USERNAME, DBHelper.NOME, DBHelper.COGNOME, DBHelper.TELEFONO, DBHelper.EMAIL)
        val cursor = db.query(
            DBHelper.TABLE_UTENTE,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun getUserIdByUsername(username: String): Int? {
        val projection = arrayOf(DBHelper.ID_UTENTE)
        val selection = "${DBHelper.USERNAME} = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(
            DBHelper.TABLE_UTENTE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var userId: Int? = null
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(DBHelper.ID_UTENTE))
        }
        cursor?.close()
        return userId
    }

    fun selectIdUtente(): Cursor {
        val projection = arrayOf(DBHelper.USERNAME)
        val cursor = db.query(
            DBHelper.TABLE_UTENTE,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }

    fun insertPrenotazioneRistorante(
        idUtente: Int,
        dataPrenotazione: String,
        numeroOspiti: Int,
        chosenMeal: String
    ) {
        val values = ContentValues().apply {
            put(DBHelper.REF_UTENTE, idUtente)
            put(DBHelper.DATA_PRENOTAZIONE, dataPrenotazione)
            put(DBHelper.NUMERO_OSPITI, numeroOspiti)
            put(DBHelper.CHOSENMEAL, chosenMeal)
        }
        db.insertWithOnConflict(DBHelper.TABLE_RISTORANTE, null, values, SQLiteDatabase.CONFLICT_IGNORE)
    }

    fun insertPrenotazioneGym(
        idUtente: Int,
        dataGym: String,
        ospitiGym: Int
    ) {
        val values = ContentValues().apply {
            put(DBHelper.REF_GYM, idUtente)
            put(DBHelper.DATA_GYM, dataGym)
            put(DBHelper.OSPITI_GYM, ospitiGym)
        }
        db.insertWithOnConflict(DBHelper.TABLE_GYM, null, values, SQLiteDatabase.CONFLICT_IGNORE)
    }




}