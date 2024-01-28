package com.example.examenib.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examenib.models.Album
import com.example.examenib.models.Artista
import java.text.SimpleDateFormat

class ArtistaRepository(
    contexto: Context?, //This
) : SQLiteOpenHelper(
    contexto,
    "artista_album", //nombre BDD
    null,
    5
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaArtista =
            """
                CREATE TABLE ARTISTA(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                fechaNacimiento TEXT,
                descripcion TEXT,
                calificacion INTEGER
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaArtista)

        val scriptSQLCrearTablaAlbum =
            """
            CREATE TABLE ALBUM(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                duracion INTEGER,
                idArtista INTEGER,
                esColaborativo INTEGER,
                fechaLanzamiento TEXT,
                FOREIGN KEY (idArtista) REFERENCES ARTISTA(id)
            )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaAlbum)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 5) {
            // Elimina las tablas si existen
            db?.execSQL("DROP TABLE IF EXISTS ALBUM")
            db?.execSQL("DROP TABLE IF EXISTS ARTISTA")

            // Vuelve a crear la tabla ARTISTA
            val scriptSQLCrearTablaArtista =
                """
                CREATE TABLE ARTISTA(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                fechaNacimiento TEXT,
                descripcion TEXT,
                calificacion INTEGER
                )
                """.trimIndent()
            db?.execSQL(scriptSQLCrearTablaArtista)

            // Vuelve a crear la tabla ALBUM con ON DELETE CASCADE
            val scriptSQLCrearTablaAlbum =
                """
                CREATE TABLE ALBUM(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                duracion INTEGER,
                idArtista INTEGER,
                esColaborativo INTEGER,
                fechaLanzamiento TEXT,
                FOREIGN KEY (idArtista) REFERENCES ARTISTA(id)
                )
                """.trimIndent()
            db?.execSQL(scriptSQLCrearTablaAlbum)

            // Resto del código permanece igual
            // ...
        }
    }

    /* CRUD Artistas */

    fun crearArtista(newArtist: Artista): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        valoresAGuardar.put("nombre", newArtist.nombre)
        valoresAGuardar.put("fechaNacimiento", SimpleDateFormat("dd/MM/yyyy").format(newArtist.fechaNacimiento))
        valoresAGuardar.put("descripcion", newArtist.descripcion)
        valoresAGuardar.put("calificacion", newArtist.calificacion)

        val resultadoGuardar = basedatosEscritura
            .insert(
                "ARTISTA", //nombre de la tabla
                null,
                valoresAGuardar //valores
            )

        basedatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun obtenerArtistas(): ArrayList<Artista> {
        val baseDatosLectura = readableDatabase

        val scriptConsultaLectura = """
            SELECT * FROM ARTISTA
        """.trimIndent()

        val resultadoConsulta = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val artistas = arrayListOf<Artista>()

        if (resultadoConsulta != null && resultadoConsulta.moveToFirst()) {

            do {
                val id = resultadoConsulta.getInt(0) //Indice 0
                val nombre = resultadoConsulta.getString(1)
                val fechaNacimiento = resultadoConsulta.getString(2)
                val descripcion = resultadoConsulta.getString(3)
                val calificacion = resultadoConsulta.getInt(4)

                if (nombre != null) {
                    val artistaEncontrado = Artista(1, "Nombre", "01/01/2000", "Descripción", 5)
                    artistaEncontrado.id = id
                    artistaEncontrado.nombre = nombre
                    artistaEncontrado.fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimiento)
                    artistaEncontrado.descripcion = descripcion
                    artistaEncontrado.calificacion = calificacion

                    artistas.add(artistaEncontrado)
                }
            } while (resultadoConsulta.moveToNext())
        }

        resultadoConsulta?.close()
        baseDatosLectura.close()
        return artistas //arreglo
    }

    fun consultarArtistaPorId(id: Int): Artista {
        val baseDatosLectura = readableDatabase

        val scriptConsultaLectura = """
            SELECT * FROM ARTISTA WHERE ID = ?
        """.trimIndent()

        val parametrosConsultaLectura = arrayOf(id.toString())

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura, //Consulta
            parametrosConsultaLectura //Parametros
        )

        val existeArtista = resultadoConsultaLectura.moveToFirst()

        val artistaEncontrado = Artista(1, "Nombre", "01/01/2000", "Descripción", 5)
        if (existeArtista) {
            do {
                val id = resultadoConsultaLectura.getInt(0) //Indice 0
                val nombre = resultadoConsultaLectura.getString(1)
                val fechaNacimiento = resultadoConsultaLectura.getString(2)
                val descripcion = resultadoConsultaLectura.getString(3)
                val calificacion = resultadoConsultaLectura.getInt(4)

                if (nombre != null) {
                    artistaEncontrado.id = id
                    artistaEncontrado.nombre = nombre
                    artistaEncontrado.fechaNacimiento = SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimiento)
                    artistaEncontrado.descripcion = descripcion
                    artistaEncontrado.calificacion = calificacion
                }
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return artistaEncontrado //arreglo
    }

    fun actualizarArtistaPorId(
        datosActualizados: Artista
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", datosActualizados.nombre)
        valoresAActualizar.put("fechaNacimiento", SimpleDateFormat("dd/MM/yyyy").format(datosActualizados.fechaNacimiento))
        valoresAActualizar.put("descripcion", datosActualizados.descripcion)
        valoresAActualizar.put("calificacion", datosActualizados.calificacion)
        //where id = ?
        val parametrosConsultaActualizar = arrayOf(datosActualizados.id.toString())
        val resultadoActualizcion = conexionEscritura
            .update(
                "ARTISTA", //tabla
                valoresAActualizar,
                "id = ?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return resultadoActualizcion != -1
    }

    fun eliminarArtistaPorId(id: Int): Boolean {
        val conexionEscritura = writableDatabase

        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminacion = conexionEscritura
            .delete(
                "ARTISTA", //tabla
                "id = ?",
                parametrosConsultaDelete
            )

        conexionEscritura.close()
        return resultadoEliminacion != -1
    }
}

class AlbumRepository(
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto,
    "artista_album",
    null,
    5
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaAlbum =
            """
            CREATE TABLE ALBUM(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                duracion INTEGER,
                idArtista INTEGER,
                esColaborativo INTEGER,
                fechaLanzamiento TEXT,
                FOREIGN KEY (idArtista) REFERENCES ARTISTA(id)
            )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaAlbum)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 5) {
            db?.execSQL("DROP TABLE IF EXISTS ALBUM")

            val scriptSQLCrearTablaAlbum =
                """
                CREATE TABLE ALBUM(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    duracion INTEGER,
                    idArtista INTEGER,
                    esColaborativo INTEGER,
                    fechaLanzamiento TEXT,
                    FOREIGN KEY (idArtista) REFERENCES ARTISTA(id)
                )
                """.trimIndent()
            db?.execSQL(scriptSQLCrearTablaAlbum)

            // Resto del código permanece igual
            // ...
        }
    }

    /* CRUD Álbumes */

    fun crearAlbum(newAlbum: Album): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()

        valoresAGuardar.put("nombre", newAlbum.nombre)
        valoresAGuardar.put("duracion", newAlbum.duracion)
        valoresAGuardar.put("idArtista", newAlbum.artista.id)
        valoresAGuardar.put("esColaborativo", if (newAlbum.esColaborativo) 1 else 0)
        valoresAGuardar.put(
            "fechaLanzamiento",
            SimpleDateFormat("dd/MM/yyyy").format(newAlbum.fechaLanzamiento)
        )

        val resultadoGuardar = basedatosEscritura
            .insert(
                "ALBUM", //nombre de la tabla
                null,
                valoresAGuardar //valores
            )

        basedatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun obtenerAlbumes(): ArrayList<Album> {
        val baseDatosLectura = readableDatabase

        val scriptConsultaLectura = """
            SELECT * FROM ALBUM
        """.trimIndent()

        val resultadoConsulta = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val albumes = arrayListOf<Album>()

        if (resultadoConsulta != null && resultadoConsulta.moveToFirst()) {

            do {
                val id = resultadoConsulta.getInt(0) //Indice 0
                val nombre = resultadoConsulta.getString(1)
                val duracion = resultadoConsulta.getInt(2)
                val idArtista = resultadoConsulta.getInt(3)
                val esColaborativo = resultadoConsulta.getInt(4)
                val fechaLanzamiento = resultadoConsulta.getString(5)

                val artistaRepository = ArtistaRepository(null)
                val artista = artistaRepository.consultarArtistaPorId(idArtista)

                if (nombre != null) {
                    val albumEncontrado = Album(
                        1,
                        "Nombre",
                        0,
                        artista,
                        false,
                        SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000")
                    )
                    albumEncontrado.id = id
                    albumEncontrado.nombre = nombre
                    albumEncontrado.duracion = duracion
                    albumEncontrado.esColaborativo = esColaborativo == 1
                    albumEncontrado.fechaLanzamiento =
                        SimpleDateFormat("dd/MM/yyyy").parse(fechaLanzamiento)

                    albumes.add(albumEncontrado)
                }
            } while (resultadoConsulta.moveToNext())
        }

        resultadoConsulta?.close()
        baseDatosLectura.close()
        return albumes //arreglo
    }

    fun consultarAlbumPorId(id: Int): Album {
        val baseDatosLectura = readableDatabase

        val scriptConsultaLectura = """
            SELECT * FROM ALBUM WHERE ID = ?
        """.trimIndent()

        val parametrosConsultaLectura = arrayOf(id.toString())

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura, //Consulta
            parametrosConsultaLectura //Parametros
        )

        val existeAlbum = resultadoConsultaLectura.moveToFirst()

        val albumEncontrado = Album(
            1,
            "Nombre",
            0,
            Artista(
                1,
                "Nombre",
                SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"),
                "Descripción",
                5
            ),
            false,
            SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000")
        )
        if (existeAlbum) {
            do {
                val id = resultadoConsultaLectura.getInt(0) //Indice 0
                val nombre = resultadoConsultaLectura.getString(1)
                val duracion = resultadoConsultaLectura.getInt(2)
                val idArtista = resultadoConsultaLectura.getInt(3)
                val esColaborativo = resultadoConsultaLectura.getInt(4)
                val fechaLanzamiento = resultadoConsultaLectura.getString(5)

                val artistaRepository = ArtistaRepository(null)
                val artista = artistaRepository.consultarArtistaPorId(idArtista)

                if (nombre != null) {
                    albumEncontrado.id = id
                    albumEncontrado.nombre = nombre
                    albumEncontrado.duracion = duracion
                    albumEncontrado.artista = artista
                    albumEncontrado.esColaborativo = esColaborativo == 1
                    albumEncontrado.fechaLanzamiento =
                        SimpleDateFormat("dd/MM/yyyy").parse(fechaLanzamiento)
                }
            } while (resultadoConsultaLectura.moveToNext())
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return albumEncontrado //arreglo
    }

    fun actualizarAlbumPorId(
        datosActualizados: Album
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", datosActualizados.nombre)
        valoresAActualizar.put("duracion", datosActualizados.duracion)
        valoresAActualizar.put("idArtista", datosActualizados.artista.id)
        valoresAActualizar.put("esColaborativo", if (datosActualizados.esColaborativo) 1 else 0)
        valoresAActualizar.put(
            "fechaLanzamiento",
            SimpleDateFormat("dd/MM/yyyy").format(datosActualizados.fechaLanzamiento)
        )
        //where id = ?
        val parametrosConsultaActualizar = arrayOf(datosActualizados.id.toString())
        val resultadoActualizcion = conexionEscritura
            .update(
                "ALBUM", //tabla
                valoresAActualizar,
                "id = ?",
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return resultadoActualizcion != -1
    }

    fun eliminarAlbumPorId(id: Int): Boolean {
        val conexionEscritura = writableDatabase

        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminacion = conexionEscritura
            .delete(
                "ALBUM", //tabla
                "id = ?",
                parametrosConsultaDelete
            )

        conexionEscritura.close()
        return resultadoEliminacion != -1
    }
}
