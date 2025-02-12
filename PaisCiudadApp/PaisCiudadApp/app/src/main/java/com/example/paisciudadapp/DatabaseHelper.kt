package com.example.paisciudadapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Pais (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE Ciudad (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                pais_id INTEGER NOT NULL,
                es_capital INTEGER DEFAULT 0,
                poblacion INTEGER,
                anio_fundacion INTEGER,
                latitud REAL,
                longitud REAL,
                FOREIGN KEY (pais_id) REFERENCES Pais(id) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Ciudad")
        db.execSQL("DROP TABLE IF EXISTS Pais")
        onCreate(db)
    }
    fun insertCiudad(nombre: String, paisId: Int, esCapital: Int, poblacion: Int, anioFundacion: Int, latitud: Double, longitud: Double): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("pais_id", paisId)
            put("es_capital", esCapital)
            put("poblacion", poblacion)
            put("anio_fundacion", anioFundacion)
            put("latitud", latitud)
            put("longitud", longitud)
        }
        return db.insert("Ciudad", null, values)
    }


    fun insertPais(nombre: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
        }
        return db.insert("Pais", null, values)
    }

    fun getAllPaises(): List<Pais> {
        val db = readableDatabase
        val paises = mutableListOf<Pais>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM Pais", null)

        while (cursor.moveToNext()) {
            val pais = Pais(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            )
            paises.add(pais)
        }
        cursor.close()
        return paises
    }
    fun getCiudadById(id: Int): Ciudad? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Ciudad WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val ciudad = Ciudad(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                paisId = cursor.getInt(cursor.getColumnIndexOrThrow("pais_id")),
                poblacion = cursor.getInt(cursor.getColumnIndexOrThrow("poblacion")),
                anioFundacion = cursor.getInt(cursor.getColumnIndexOrThrow("anio_fundacion")),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
            )
            cursor.close()
            ciudad
        } else {
            cursor.close()
            null
        }
    }
    fun updateCiudad(id: Int, nombre: String, poblacion: Int?, anioFundacion: Int?, latitud: Double?, longitud: Double?) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("poblacion", poblacion ?: 0)
            put("anio_fundacion", anioFundacion ?: 0)
            put("latitud", latitud ?: 0.0)
            put("longitud", longitud ?: 0.0)
        }
        db.update("Ciudad", values, "id = ?", arrayOf(id.toString()))
    }

    fun getCiudadesByPais(paisId: Int): List<Ciudad> {
        val db = readableDatabase
        val ciudades = mutableListOf<Ciudad>()
        val cursor: Cursor = db.rawQuery("SELECT * FROM Ciudad WHERE pais_id = ?", arrayOf(paisId.toString()))

        while (cursor.moveToNext()) {
            val ciudad = Ciudad(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                paisId = cursor.getInt(cursor.getColumnIndexOrThrow("pais_id")),
                poblacion = cursor.getInt(cursor.getColumnIndexOrThrow("poblacion")),
                anioFundacion = cursor.getInt(cursor.getColumnIndexOrThrow("anio_fundacion")),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
            )
            ciudades.add(ciudad)
        }
        cursor.close()
        return ciudades
    }
    fun getAllCiudades(): List<Ciudad> {
        val db = readableDatabase
        val ciudades = mutableListOf<Ciudad>()
        val cursor = db.rawQuery("SELECT * FROM Ciudad", null)

        while (cursor.moveToNext()) {
            val ciudad = Ciudad(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                paisId = cursor.getInt(cursor.getColumnIndexOrThrow("pais_id")),
                poblacion = cursor.getInt(cursor.getColumnIndexOrThrow("poblacion")),
                anioFundacion = cursor.getInt(cursor.getColumnIndexOrThrow("anio_fundacion")),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud")),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
            )
            ciudades.add(ciudad)
        }
        cursor.close()
        return ciudades
    }

    companion object {
        private const val DATABASE_NAME = "PaisCiudadDB"
        private const val DATABASE_VERSION = 2
    }
}

data class Pais(val id: Int, val nombre: String)
data class Ciudad(
    val id: Int,
    val nombre: String,
    val paisId: Int,
    val poblacion: Int,
    val anioFundacion: Int,
    val latitud: Double,
    val longitud: Double
)
