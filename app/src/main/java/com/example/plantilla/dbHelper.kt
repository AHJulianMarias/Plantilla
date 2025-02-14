package com.example.plantilla

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class dbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    //Sin corrutinas las funciones sin suspend
    companion object {
        private const val DATABASE_NAME = "Viajes"
        private const val DATABASE_VERSION = 1
        private const val TABLE_VIAJES = "Viaje"
        private const val KEY_ID = "Id"
        private const val KEY_ORIGEN = "origen"
        private const val KEY_DESTINO = "destino"
        private const val KEY_FECHA = "fecha"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createViajesTable = ("CREATE TABLE " + TABLE_VIAJES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORIGEN + " TEXT,"
                + KEY_DESTINO + " TEXT,"
                + KEY_FECHA + " TEXT " +
                ")")

        db?.execSQL(createViajesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VIAJES")
        onCreate(db)
    }

    fun getAllViajes(): ArrayList<Viaje> {
        val ViajesList = ArrayList<Viaje>()
        val selectQuery = "SELECT * FROM $TABLE_VIAJES"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var origen: String
        var destino: String
        var fecha:String


        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_ORIGEN)
                val destinoIndex = cursor.getColumnIndex(KEY_DESTINO)
                val fechaIndex =cursor.getColumnIndex(KEY_FECHA)

                if (idIndex != -1 && nombreIndex != -1 && nombreIndex != -1 && destinoIndex != -1 && fechaIndex != -1) {
                    id = cursor.getInt(idIndex)
                    origen = cursor.getString(nombreIndex)
                    destino = cursor.getString(destinoIndex)
                    fecha = cursor.getString(fechaIndex)

                    /**
                     * data class Viaje (
                     *     var fecha:String,
                     *     var origen:String,
                     *     var destino:String
                     * )
                     */
                    val viaje = Viaje(id = id,
                        fecha = fecha,
                        origen = origen,
                        destino = destino)
                    ViajesList.add(viaje)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return ViajesList
    }

    fun updateViaje(Viaje: Viaje): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ORIGEN, Viaje.origen)
        contentValues.put(KEY_DESTINO, Viaje.destino)
        contentValues.put(KEY_FECHA, Viaje.fecha)
        return db.update(TABLE_VIAJES, contentValues, "$KEY_ID = ?", arrayOf(Viaje.id.toString()))
    }

     fun deleteViaje(id:Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_VIAJES, "$KEY_ID = ?", arrayOf(arrayOf(id).toString()))
        db.close()
        return success
    }

    fun addViaje(Viaje: Viaje): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_ORIGEN, Viaje.origen)
            contentValues.put(KEY_DESTINO, Viaje.destino)
            contentValues.put(KEY_FECHA, Viaje.fecha)
            val success = db.insert(TABLE_VIAJES, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            Log.e("Error", "Error al agregar Viaje", e)
            return -1
        }
    }
}


