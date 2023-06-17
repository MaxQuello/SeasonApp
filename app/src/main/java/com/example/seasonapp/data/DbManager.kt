package com.example.seasonapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

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

    fun insertUtente(id: Int, nome: String, cognome: String, email: String, password: String){
        val value = ContentValues().apply {
            put(DBHelper.ID_UTENTE, id)
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.EMAIL, email)
            put(DBHelper.PASSWORD, password)
        }
        db.insert(DBHelper.TABLE_UTENTE, null, value)
    }

    fun updateUtente(idUtente: Int, nome: String, cognome: String, telefono: String, email: String): Int{
        val selection = "${DBHelper.ID_UTENTE} = ?"
        val selectioArgs = arrayOf(idUtente.toString())
        val values = ContentValues().apply {
            put(DBHelper.ID_UTENTE, idUtente)
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.TELEFONO, telefono)
            put(DBHelper.EMAIL, email)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updateTelefono(idUtente: Int, telefono: String): Int {
        val selection = "${DBHelper.ID_UTENTE} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.TELEFONO, telefono)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updateEmail(idUtente: Int, email: String): Int {
        val selection = "${DBHelper.ID_UTENTE} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.EMAIL, email)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun updatePassword(idUtente: Int, password: String): Int {
        val selection = "${DBHelper.ID_UTENTE} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.PASSWORD, password)
        }

        return db.update(DBHelper.TABLE_UTENTE, values, selection, selectioArgs)
    }

    fun deleteUtente(idUtente: Int){
        val selection = "${DBHelper.ID_UTENTE} = ?"
        val selectionArgs = arrayOf(idUtente.toString())
        db.delete(DBHelper.TABLE_UTENTE, selection, selectionArgs)
    }

    fun deleteAll(){
        db.delete(DBHelper.TABLE_UTENTE, null, null)
    }

    fun selectUtente(): Cursor{
        val projection = arrayOf(DBHelper.ID_UTENTE, DBHelper.EMAIL, DBHelper.PASSWORD)
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
        val projection = arrayOf(DBHelper.ID_UTENTE, DBHelper.NOME, DBHelper.COGNOME, DBHelper.TELEFONO, DBHelper.EMAIL)
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
        val projection = arrayOf(DBHelper.ID_UTENTE)
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


}