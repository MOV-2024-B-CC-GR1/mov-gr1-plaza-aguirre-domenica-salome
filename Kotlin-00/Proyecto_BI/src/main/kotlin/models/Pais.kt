package main.models

data class Pais(
    val nombre: String,
    val codigo: String,
    val fechaIndependencia: String,
    val esIndependiente: Boolean,
    val poblacion: Int,
    val ciudades: MutableList<Ciudad> = mutableListOf()  // Relación uno a muchos
)


