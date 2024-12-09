package com.example.crud_paises_ciudades.utils

import com.example.crud_paises_ciudades.models.Ciudad
import com.example.crud_paises_ciudades.models.Pais

object CrudUtils {

    fun agregarPais(paises: MutableList<Pais>, nuevoPais: Pais) {
        paises.add(nuevoPais)
    }

    fun actualizarPais(paises: MutableList<Pais>, nombrePais: String, paisActualizado: Pais) {
        val indice = paises.indexOfFirst { it.nombre == nombrePais }
        if (indice != -1) {
            paises[indice] = paisActualizado
        }
    }

    fun eliminarPais(paises: MutableList<Pais>, nombrePais: String) {
        paises.removeIf { it.nombre == nombrePais }
    }

    fun agregarCiudad(pais: Pais, nuevaCiudad: Ciudad) {
        pais.ciudades.add(nuevaCiudad)
    }

    fun eliminarCiudad(pais: Pais, nombreCiudad: String) {
        pais.ciudades.removeIf { it.nombre == nombreCiudad }
    }
}

