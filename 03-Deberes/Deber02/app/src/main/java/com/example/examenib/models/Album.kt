package com.example.examenib.models

import java.text.SimpleDateFormat
import java.util.Date

class Album(
    var id: Int,
    var nombre: String,
    var duracion: Int,
    var artista: Artista,
    var esColaborativo: Boolean,
    var fechaLanzamiento: Date
) {
    // Constructor adicional para adaptar la fecha de lanzamiento
    constructor(
        id: Int,
        nombre: String,
        duracion: Int,
        artista: Artista,
        esColaborativo: Boolean,
        fechaLanzamiento: String
    ) : this(
        id,
        nombre,
        duracion,
        artista,
        esColaborativo,
        SimpleDateFormat("dd/MM/yyyy").parse(fechaLanzamiento)
    )

    fun checkEsColaborativo(esColaborativo: Boolean): String {
        return if (esColaborativo) "Si" else "No"
    }

    override fun toString(): String {
        return "\nID: $id " +
                "\nNombre del Álbum: $nombre " +
                "\nDuración: $duracion minutos" +
                "\nArtista: ${artista.nombre}" +
                "\n${checkEsColaborativo(esColaborativo)} es un álbum colaborativo" +
                "\nFecha de Lanzamiento: ${SimpleDateFormat("dd/MM/yyyy").format(fechaLanzamiento)}\n"
    }
}
