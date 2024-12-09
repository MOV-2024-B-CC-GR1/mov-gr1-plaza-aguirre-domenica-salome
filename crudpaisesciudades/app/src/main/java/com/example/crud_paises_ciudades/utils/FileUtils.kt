package com.example.crud_paises_ciudades.utils

import android.content.Context
import com.example.crud_paises_ciudades.models.Ciudad
import com.example.crud_paises_ciudades.models.Pais
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object FileUtils {

    private const val FILE_NAME = "paises.json"

    fun leerPaises(context: Context): MutableList<Pais> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            return mutableListOf()
        }

        val json = file.readText()
        val tipo = object : TypeToken<MutableList<Pais>>() {}.type
        return Gson().fromJson(json, tipo) ?: mutableListOf()
    }

    fun guardarPaises(context: Context, paises: MutableList<Pais>) {
        val file = File(context.filesDir, FILE_NAME)
        val json = Gson().toJson(paises)
        file.writeText(json)
    }
}

