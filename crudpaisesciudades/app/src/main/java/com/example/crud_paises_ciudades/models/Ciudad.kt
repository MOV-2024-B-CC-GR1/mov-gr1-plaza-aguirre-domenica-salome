package com.example.crud_paises_ciudades.models

data class Ciudad(
    val nombre: String,
    val poblacion: Int,
    val area: Double,
    val tieneAeropuerto: Boolean,
    val fechaFundacion: String
)
