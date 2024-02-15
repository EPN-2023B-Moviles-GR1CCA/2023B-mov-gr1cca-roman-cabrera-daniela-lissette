package com.example.examenib.models

import java.util.Date

class Album(
    var id: Int,
    var nombre: String,
    var duracion: Int,
    var artista: Artista,
    var esColaborativo: Boolean,
    var fechaLanzamiento: Date
) {
    override fun toString(): String {
        return "${nombre}"
    }
}

