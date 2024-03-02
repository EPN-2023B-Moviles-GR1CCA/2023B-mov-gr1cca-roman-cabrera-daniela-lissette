package com.example.exameniib.models

import java.util.*

data class Album(
    var id: Int = 0,
    var nombre: String = "",
    var duracion: Int = 0,
    var artistaId: Int = 0,
    var esColaborativo: Boolean = false,
    var fechaLanzamiento: Date = Date()
)

