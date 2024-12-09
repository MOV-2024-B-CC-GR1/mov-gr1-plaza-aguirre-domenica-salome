package main.utils

import java.io.File

object FileUtils {
    // Crea el archivo de país si no existe
    fun crearArchivoPais() {
        val archivo = File("pais.json")
        if (!archivo.exists()) {
            archivo.writeText("[]") // Inicializa el archivo con una lista vacía de países
        }
    }

    // Crea el archivo de ciudad si no existe
    fun crearArchivoCiudad() {
        val archivo = File("ciudad.json")
        if (!archivo.exists()) {
            archivo.writeText("[]") // Inicializa el archivo con una lista vacía de ciudades
        }
    }
}
