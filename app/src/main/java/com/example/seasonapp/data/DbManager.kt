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

    fun insertUtente(username:String, nome: String, cognome: String, email: String, password: String){
        val value = ContentValues().apply {
            put(DBHelper.USERNAME, username)
            put(DBHelper.NOME, nome)
            put(DBHelper.COGNOME, cognome)
            put(DBHelper.EMAIL, email)
            put(DBHelper.PASSWORD, password)
        }
        db.insert(DBHelper.TABLE_PERSONA, null, value)
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

        return db.update(DBHelper.TABLE_PERSONA, values, selection, selectioArgs)
    }

    fun updateTelefono(idUtente: Int, telefono: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.TELEFONO, telefono)
        }

        return db.update(DBHelper.TABLE_PERSONA, values, selection, selectioArgs)
    }

    fun updateEmail(idUtente: Int, email: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.EMAIL, email)
        }

        return db.update(DBHelper.TABLE_PERSONA, values, selection, selectioArgs)
    }

    fun updatePassword(idUtente: Int, password: String): Int {
        val selection = "${DBHelper.USERNAME} = ?"
        val selectioArgs = arrayOf(idUtente.toString())

        val values = ContentValues().apply {
            put(DBHelper.PASSWORD, password)
        }

        return db.update(DBHelper.TABLE_PERSONA, values, selection, selectioArgs)
    }

    fun deleteUtente(idUtente: Int){
        val selection = "${DBHelper.USERNAME} = ?"
        val selectionArgs = arrayOf(idUtente.toString())
        db.delete(DBHelper.TABLE_PERSONA, selection, selectionArgs)
    }

    fun deleteAll(){
        db.delete(DBHelper.TABLE_PERSONA, null, null)
    }

    fun selectUtente(): Cursor{
        val projection = arrayOf(DBHelper.USERNAME, DBHelper.EMAIL, DBHelper.PASSWORD)
        val cursor = db.query(
            DBHelper.TABLE_PERSONA,
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
            DBHelper.TABLE_PERSONA,
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
            DBHelper.TABLE_PERSONA,
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