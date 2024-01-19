package com.example.examenib.database

import java.util.Date

class CrudAlbum {
    fun crearAlbum(
        id: Int,
        nombre: String,
        fechaLanzamiento: Date,
        duracion: Int,
        idArtista: Int, // ID del artista asociado al álbum
        esColaborativo: Boolean
    ) {
        val album = Album(
            id,
            nombre,
            duracion,
            idArtista, // Asignar el ID del artista al álbum
            esColaborativo,
            fechaLanzamiento
        )

        DBMemoria.arregloAlbum.add(album)
    }

    fun editarAlbum(
        id: Int,
        nombre: String,
        fechaLanzamiento: Date,
        duracion: Int,
        idArtista: Int, // ID del artista asociado al álbum
        esColaborativo: Boolean
    ) {
        val album = Album(
            id,
            nombre,
            duracion,
            idArtista, // Asignar el ID del artista al álbum
            esColaborativo,
            fechaLanzamiento
        )

        var posicion = -1

        DBMemoria.arregloAlbum.forEachIndexed { index, album ->
            if (album.id == id) {
                posicion = index
            }
        }
        DBMemoria.arregloAlbum[posicion] = album
    }
}
