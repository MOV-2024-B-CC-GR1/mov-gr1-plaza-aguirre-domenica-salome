package com.example.crud_paises_ciudades.models

data class Pais(
    val nombre: String,
    val codigo: String,
    val fechaIndependencia: String,
    val esDesarrollado: Boolean,
    val poblacion: Int,
    val ciudades: MutableList<Ciudad> = mutableListOf()
)
