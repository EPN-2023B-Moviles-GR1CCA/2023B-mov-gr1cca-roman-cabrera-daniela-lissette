package com.example.exameniib.models

import java.util.*

data class Artista (
    var id: Int = 0,
    var nombre: String = "",
    var fechaNacimiento: Date = Date(),
    var descripcion: String = "",
    var calificacion: Int = 0
)