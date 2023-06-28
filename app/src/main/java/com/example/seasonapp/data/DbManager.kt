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

    fun insertUtente(nome: String,cognome:String,gender:String,dataNascita:String,email:String,telefono: String,
    username:String,password: String,risposta:String){
        val value = ContentValues().apply {
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.GENDER, gender)
            put(DBHelper.EMAIL, email)
            put(DBHelper.DATANASCITA, dataNascita)
            put(DBHelper.EMAIL,email)
            put(DBHelper.TELEFONO,telefono)
            put(DBHelper.USERNAME,username)
            put(DBHelper.PASSWORD,password)
            put(DBHelper.RISPOSTA,risposta)
        }
        db.insert(DBHelper.TABLE_UTENTE, null, value)
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

    fun insertPrenotazioneRistorante(data_prenotazione:String,numero_ospiti:Int,value_checked : Boolean){
        val value = ContentValues().apply {
            put(DBHelper.DATA_PRENOTAZIONE,data_prenotazione)
            put(DBHelper.NUMERO_OSPITI,numero_ospiti)
            put(DBHelper.VALUE_CHECKED,value_checked)
        }
        db.insert(DBHelper.TABLE_RISTORANTE,null,value)
    }


}