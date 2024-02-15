package com.example.examenib.database

import com.example.examenib.models.Artista
import java.util.Date

class CrudArtista {
    fun crearArtista(
        id: Int,
        nombre: String,
        fechaNacimiento: Date,
        descripcion: String,
        calificacion: Int
    ) {
        val artista = Artista(
            id,
            nombre,
            fechaNacimiento,
            descripcion,
            calificacion
        )
        BDMemoria.arregloArtista.add(artista)
    }

    fun editarArtista(
        id: Int,
        nombre: String,
        fechaNacimiento: Date,
        descripcion: String,
        calificacion: Int
    ) {
        val artista = Artista(
            id,
            nombre,
            fechaNacimiento,
            descripcion,
            calificacion
        )

        val artistaAux = BDMemoria.arregloArtista.find { artista -> artista.id == id
        }

        val posicion = BDMemoria.arregloArtista.indexOf(artistaAux)
        BDMemoria.arregloArtista[posicion] = artista
    }

    fun eliminarAlbumesDelArtista(id: Int) {
        val albumes = BDMemoria.arregloAlbum.filter { album ->  album.artista.id == id
        }

        albumes.forEach { album ->
            BDMemoria.arregloAlbum.removeIf { albumAux ->
                albumAux.id == album.id
            }
        }
    }

}
