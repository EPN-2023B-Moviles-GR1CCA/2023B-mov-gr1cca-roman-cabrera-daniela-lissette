package com.example.examenib.models

import java.text.SimpleDateFormat
import java.util.Date

class Artista(
    var id: Int,
    var nombre: String,
    var fechaNacimiento: Date,
    var descripcion: String,
    var calificacion: Int
) {
    // Constructor adicional para adaptar la fecha de nacimiento
    constructor(
        id: Int,
        nombre: String,
        fechaNacimiento: String,
        descripcion: String,
        calificacion: Int
    ) : this(
        id,
        nombre,
        SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimiento),
        descripcion,
        calificacion
    )

    override fun toString(): String {
        return "\nID: $id " +
                "\nNombre: $nombre " +
                "\nFecha de Nacimiento: ${SimpleDateFormat("dd/MM/yyyy").format(fechaNacimiento)}" +
                "\nDescripción: $descripcion" +
                "\nCalificación: $calificacion\n"
    }
}
