package main.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import main.models.Pais
import main.models.Ciudad
import java.io.File

object CrudUtils {
    private val gson = Gson()

    // Guardar los datos de Pais en el archivo JSON
    fun guardarPais(pais: Pais) {
        val paises = obtenerPaises().toMutableList() // Obtiene la lista de países existentes
        paises.add(pais) // Añade el nuevo país a la lista
        File("pais.json").writeText(gson.toJson(paises)) // Sobrescribe el archivo con la lista actualizada
    }

    // Guardar los datos de Ciudad en el archivo JSON
    fun guardarCiudad(ciudad: Ciudad) {
        val pais = obtenerPais(ciudad.pais) // Obtiene el país de la ciudad
        pais?.ciudades?.add(ciudad) // Añade la ciudad a la lista de ciudades del país
        pais?.let { actualizarPais(it) } // Actualiza el país con la nueva ciudad
    }

    // Leer el archivo de Pais y devolver la lista de objetos
    fun obtenerPaises(): List<Pais> {
        // Si el archivo no existe o está vacío, devolvemos una lista vacía.
        val paisJson = File("pais.json").takeIf { it.exists() }?.readText() ?: "[]"
        val paisType = object : TypeToken<List<Pais>>() {}.type
        return gson.fromJson(paisJson, paisType) // Convierte el JSON en una lista de objetos Pais
    }

    // Obtener un país por su nombre
    fun obtenerPais(nombrePais: String): Pais? {
        return obtenerPaises().find { it.nombre == nombrePais }
    }

    // Leer todas las ciudades de un país
    fun obtenerCiudades(nombrePais: String): List<Ciudad> {
        val pais = obtenerPais(nombrePais)
        return pais?.ciudades ?: emptyList() // Retorna las ciudades del país o una lista vacía si no tiene
    }

    // Eliminar un país
    fun eliminarPais(nombrePais: String) {
        val paises = obtenerPaises().toMutableList()
        paises.removeIf { it.nombre == nombrePais } // Elimina el país por su nombre
        File("pais.json").writeText(gson.toJson(paises)) // Sobrescribe el archivo con los países restantes
    }

    // Actualizar los datos de un País
    fun actualizarPais(pais: Pais) {
        val paises = obtenerPaises().toMutableList()
        paises.removeIf { it.nombre == pais.nombre } // Elimina el país antiguo
        paises.add(pais) // Añade el país actualizado
        File("pais.json").writeText(gson.toJson(paises)) // Sobrescribe el archivo con los países actualizados
    }

    // Actualizar los datos de una Ciudad
    fun actualizarCiudad(ciudad: Ciudad) {
        val pais = obtenerPais(ciudad.pais) // Obtiene el país correspondiente
        pais?.ciudades?.removeIf { it.nombre == ciudad.nombre } // Elimina la ciudad vieja
        pais?.ciudades?.add(ciudad) // Añade la ciudad actualizada
        pais?.let { actualizarPais(it) } // Actualiza el país con la nueva ciudad
    }
}
