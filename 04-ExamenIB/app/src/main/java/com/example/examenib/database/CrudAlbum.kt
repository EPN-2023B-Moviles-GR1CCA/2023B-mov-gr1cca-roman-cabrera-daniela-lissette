package com.example.examenib.database

import com.example.examenib.models.Album
import java.util.Date


class CrudAlbum {
    fun crearAlbum(
        id: Int,
        nombre: String,
        fechaLanzamiento: Date,
        duracion: Int,
        idArtista: Int,
        esColaborativo: Boolean
    ) {
        val album = Album(
            id,
            nombre,
            duracion,
            BDMemoria.arregloArtista.find { artista -> artista.id == idArtista }!!,
            esColaborativo,
            fechaLanzamiento
        )
        BDMemoria.arregloAlbum.add(album)
    }

    fun editarAlbum(
        id: Int,
        nombre: String,
        fechaLanzamiento: Date,
        duracion: Int,
        idArtista: Int,
        esColaborativo: Boolean
    ) {
        val album = Album(
            id,
            nombre,
            duracion,
            BDMemoria.arregloArtista.find { artista -> artista.id == idArtista }!!,
            esColaborativo,
            fechaLanzamiento
        )

        var posicion = -1

        BDMemoria.arregloAlbum.forEachIndexed { index, album ->
            if (album.id == id) {
                posicion = index
            }
        }
        BDMemoria.arregloAlbum[posicion] = album
    }
}
