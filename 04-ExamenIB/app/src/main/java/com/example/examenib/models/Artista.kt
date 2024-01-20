package com.example.examenib.models

import java.util.*

class Artista (
        var id: Int,
        var nombre: String,
        var fechaNacimiento: Date,
        var descripcion: String,
        var calificacion: Int
){
    override fun toString(): String {
        return "${nombre}"
    }
}