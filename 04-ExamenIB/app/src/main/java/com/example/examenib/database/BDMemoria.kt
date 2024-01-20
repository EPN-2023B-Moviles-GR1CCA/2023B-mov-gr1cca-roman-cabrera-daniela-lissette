package com.example.examenib.database

import com.example.examenib.models.Album
import com.example.examenib.models.Artista
import java.text.ParseException
import java.text.SimpleDateFormat

class BDMemoria {

    companion object {
        val arregloArtista = arrayListOf<Artista>()
        val arregloAlbum = arrayListOf<Album>()

        init {
                arregloArtista.add(
                    Artista(
                        1,
                        "Queen",
                        SimpleDateFormat("dd/MM/yyyy").parse("12/06/1970"),
                        "Banda británica de rock formada en Londres",
                        9
                    )
                )

                arregloArtista.add(
                    Artista(
                        2,
                        "The Beatles",
                        SimpleDateFormat("dd/MM/yyyy").parse("12/06/1960"),
                        "Banda británica de rock que revolucionó la música",
                        10
                    )
                )

                arregloArtista.add(
                    Artista(
                        3,
                        "Michael Jackson",
                        SimpleDateFormat("dd/MM/yyyy").parse("29/08/1958"),
                        "Rey del pop y uno de los artistas más exitosos de todos los tiempos",
                        8
                    )
                )

                arregloArtista.add(
                    Artista(
                        4,
                        "AC/DC",
                        SimpleDateFormat("dd/MM/yyyy").parse("31/12/1973"),
                        "Banda australiana de hard rock formada en Sídney",
                        9
                    )
                )


                // ARREGLOS PARA ALBUM

                arregloAlbum.add(
                    Album(
                        1,
                        "A Night at the Opera",
                        60,
                        arregloArtista.find { artista ->
                            artista.id == 1
                        }!!,
                        true,  // Álbum en vivo
                        SimpleDateFormat("dd/MM/yyyy").parse("21/11/1975")
                    )
                )


                arregloAlbum.add(
                    Album(
                        2,
                        "Sgt. Pepper's Lonely Hearts Club Band",
                        57,
                        arregloArtista.find { artista ->
                            artista.id == 2
                        }!!,
                        false,  // No es un álbum en vivo
                        SimpleDateFormat("dd/MM/yyyy").parse("26/05/1967")
                    )
                )


                arregloAlbum.add(
                    Album(
                        3,
                        "Thriller",
                        60,
                        arregloArtista.find { artista ->
                            artista.id == 3
                        }!!,
                        true,
                        SimpleDateFormat("dd/MM/yyyy").parse("30/11/1982")
                    )
                )

                arregloAlbum.add(
                    Album(
                        4,
                        "Bad",
                        55,
                        arregloArtista.find { artista ->
                            artista.id == 3
                        }!!,
                        false,
                        SimpleDateFormat("dd/MM/yyyy").parse("31/08/1987")
                    )
                )

                arregloAlbum.add(
                    Album(
                        5,
                        "Back in Black",
                        58,
                        arregloArtista.find { artista ->
                            artista.id == 4
                        }!!,
                        true,
                        SimpleDateFormat("dd/MM/yyyy").parse("25/07/1980")
                    )
                )
        }
    }
}
