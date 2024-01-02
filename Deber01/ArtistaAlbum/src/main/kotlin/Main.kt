import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


// Archivos de datos
private val ARTISTAS_FILE = "artistas.txt"
private val ALBUMES_FILE = "albumes.txt"
private val UTF8_ENCODING = "UTF-8"


// Función principal
fun main() {
    var artistas = mutableListOf<Artista>()
    var albumes = mutableListOf<Album>()

    // Cargar datos desde archivos al iniciar
    cargarDatos(artistas, albumes)

    var opcion: Int

    // Menú principal
    do {
        println("********** Menú Principal **********")
        println("1. CRUD de Artistas")
        println("2. CRUD de Álbumes")
        println("0. Salir")
        print("Ingrese la opción: ")
        opcion = readLine()?.toIntOrNull() ?: -1

        when (opcion) {
            1 -> menuArtistas(artistas, albumes)
            2 -> menuAlbumes(albumes, artistas)
            0 -> {
                guardarDatos(artistas, albumes)
                println("Saliendo del programa. ¡Hasta luego!")
            }
            else -> println("Opción no válida. Intente de nuevo.")
        }
    } while (opcion != 0)
}

// Menú CRUD para Artistas
private fun menuArtistas(artistas: MutableList<Artista>, albumes: MutableList<Album>) {
    var opcion: Int

    do {
        println("\n********** CRUD de Artistas **********")
        println("1. Crear Artista")
        println("2. Leer Artistas")
        println("3. Actualizar Artista")
        println("4. Eliminar Artista")
        println("0. Volver al Menú Principal")
        print("Ingrese la opción: ")
        opcion = readLine()?.toIntOrNull() ?: -1

        when (opcion) {
            1 -> crearArtista(artistas)
            2 -> leerArtistas(artistas)
            3 -> actualizarArtista(artistas)
            4 -> eliminarArtista(artistas, albumes)
            0 -> println("Volviendo al Menú Principal.")
            else -> println("Opción no válida. Intente de nuevo.")
        }
    } while (opcion != 0)
}


// Menú CRUD para Álbumes
private fun menuAlbumes(albumes: MutableList<Album>, artistas: List<Artista>) {
    var opcion: Int

    do {
        println("\n********** CRUD de Álbumes **********")
        println("1. Crear Álbum")
        println("2. Leer Álbumes")
        println("3. Actualizar Álbum")
        println("4. Eliminar Álbum")
        println("0. Volver al Menú Principal")
        print("Ingrese la opción: ")
        opcion = readLine()?.toIntOrNull() ?: -1

        when (opcion) {
            1 -> crearAlbum(albumes, artistas)
            2 -> leerAlbumes(albumes)
            3 -> actualizarAlbum(albumes)
            4 -> eliminarAlbum(albumes)
            0 -> println("Volviendo al Menú Principal.")
            else -> println("Opción no válida. Intente de nuevo.")
        }
    } while (opcion != 0)
}


// Cargar datos desde archivos
private fun cargarDatos(artistas: MutableList<Artista>, albumes: MutableList<Album>) {
    try {
        ObjectInputStream(FileInputStream(ARTISTAS_FILE)).use { artistasInput ->
            @Suppress("UNCHECKED_CAST")
            artistas.addAll(artistasInput.readObject() as List<Artista>)
        }
    } catch (e: Exception) {
        // Si hay un error o el archivo no existe, no hacer nada
    }

    try {
        ObjectInputStream(FileInputStream(ALBUMES_FILE)).use { albumesInput ->
            @Suppress("UNCHECKED_CAST")
            albumes.addAll(albumesInput.readObject() as List<Album>)
        }
    } catch (e: Exception) {
        // Si hay un error o el archivo no existe, no hacer nada
    }
}

// Guardar datos en archivos
private fun guardarDatos(artistas: List<Artista>, albumes: List<Album>) {
    try {
        ObjectOutputStream(FileOutputStream(ARTISTAS_FILE)).use { artistasOutput ->
            artistasOutput.writeObject(artistas)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    try {
        ObjectOutputStream(FileOutputStream(ALBUMES_FILE)).use { albumesOutput ->
            albumesOutput.writeObject(albumes)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Función para crear un nuevo artista
private fun crearArtista(artistas: MutableList<Artista>) {
    println("Ingrese el nombre del artista:")
    val nombre = readLine() ?: ""

    val fechaNacimiento = solicitarFecha("Ingrese la fecha de nacimiento del artista (dd/MM/yyyy):")

    println("Ingrese si el artista está activo (true/false):")
    val activo = readLine()?.toBoolean() ?: false

    println("Ingrese el número de álbumes del artista:")
    val numeroAlbumes = readLine()?.toIntOrNull() ?: 0

    println("Ingrese el valor total de ventas del artista:")
    val valorTotalVentas = readLine()?.toDoubleOrNull() ?: 0.0

    val nuevoArtista = Artista(nombre, fechaNacimiento, activo, numeroAlbumes, valorTotalVentas)
    artistas.add(nuevoArtista)
    println("Artista creado con éxito: $nuevoArtista")
}

private fun solicitarFecha(mensaje: String): Date {
    while (true) {
        try {
            println(mensaje)
            val fechaIngresada = readLine() ?: ""
            val formato = SimpleDateFormat("dd/MM/yyyy")
            formato.isLenient = false // Desactivar tolerancia para fechas inválidas
            val fecha = formato.parse(fechaIngresada)
            return fecha
        } catch (e: ParseException) {
            println("Fecha no válida. Intente nuevamente.")
        }
    }
}

// Función para leer la lista de artistas
private fun leerArtistas(artistas: List<Artista>) {
    if (artistas.isEmpty()) {
        println("No hay artistas registrados.")
    } else {
        println("Lista de Artistas:")
        artistas.forEachIndexed { index, artista ->
            println("${index + 1}. $artista")
        }
    }
}

// Función para actualizar un artista existente
private fun actualizarArtista(artistas: MutableList<Artista>) {
    println("Ingrese el nombre del artista que desea actualizar:")
    val nombreBuscar = readLine() ?: ""

    val indice = artistas.indexOfFirst { it.nombre == nombreBuscar }
    if (indice != -1) {
        val artistaActualizado = crearArtistaManual(artistas[indice])
        artistas[indice] = artistaActualizado
        println("Artista actualizado con éxito: $artistaActualizado")
    } else {
        println("Artista no encontrado.")
    }
}

// Función para crear un artista manualmente con opción de mantener valores actuales
private fun crearArtistaManual(artistaExistente: Artista): Artista {
    println("Ingrese el nuevo nombre del artista (o deje en blanco para mantener el actual: ${artistaExistente.nombre}):")
    val nuevoNombre = readLine() ?: artistaExistente.nombre

    val nuevaFechaNacimiento = solicitarFecha("Ingrese la nueva fecha de nacimiento del artista (o deje en blanco para mantener la actual: ${artistaExistente.fechaNacimiento}):")

    println("Ingrese si el artista está activo (true/false, o deje en blanco para mantener el actual: ${artistaExistente.activo}):")
    val nuevoActivo = readLine()?.toBoolean() ?: artistaExistente.activo

    println("Ingrese el nuevo número de álbumes del artista (o deje en blanco para mantener el actual: ${artistaExistente.numeroAlbumes}):")
    val nuevoNumeroAlbumes = readLine()?.toIntOrNull() ?: artistaExistente.numeroAlbumes

    println("Ingrese el nuevo valor total de ventas del artista (o deje en blanco para mantener el actual: ${artistaExistente.valorTotalVentas}):")
    val nuevoValorTotalVentas = readLine()?.toDoubleOrNull() ?: artistaExistente.valorTotalVentas

    return Artista(nuevoNombre, nuevaFechaNacimiento, nuevoActivo, nuevoNumeroAlbumes, nuevoValorTotalVentas)
}

// Función para eliminar un artista y sus álbumes asociados
private fun eliminarArtista(artistas: MutableList<Artista>, albumes: MutableList<Album>) {
    println("Ingrese el nombre del artista que desea eliminar:")
    val nombreEliminar = readLine() ?: ""

    val indice = artistas.indexOfFirst { it.nombre == nombreEliminar }
    if (indice != -1) {
        val artistaEliminado = artistas.removeAt(indice)
        println("Artista eliminado con éxito: $artistaEliminado")

        // Eliminar álbumes asociados al artista eliminado
        val albumesEliminar = albumes.filter { it.artistaNombre == nombreEliminar }
        albumes.removeAll(albumesEliminar)
        println("Álbumes asociados al artista también eliminados.")
    } else {
        println("Artista no encontrado.")
    }
}

// Función para crear un nuevo álbum
private fun crearAlbum(albumes: MutableList<Album>, artistas: List<Artista>) {
    println("Ingrese el nombre del álbum:")
    val nombre = readLine() ?: ""

    val fechaLanzamiento = solicitarFecha("Ingrese la fecha de lanzamiento del álbum (dd/MM/yyyy):")

    println("Ingrese el número de canciones del álbum:")
    val numeroCanciones = readLine()?.toIntOrNull() ?: 0

    println("Ingrese el precio del álbum:")
    val precio = readLine()?.toDoubleOrNull() ?: 0.0

    println("Ingrese el nombre del artista del álbum:")
    val artistaNombre = readLine() ?: ""

    val artistaExistente = artistas.find { it.nombre == artistaNombre }
    if (artistaExistente != null) {
        val nuevoAlbum = Album(nombre, fechaLanzamiento, numeroCanciones, precio, artistaNombre)
        albumes.add(nuevoAlbum)
        println("Álbum creado con éxito: $nuevoAlbum")
    } else {
        println("Artista no encontrado. No se puede crear el álbum.")
    }
}

// Función para leer la lista de álbumes
private fun leerAlbumes(albumes: List<Album>) {
    if (albumes.isEmpty()) {
        println("No hay álbumes registrados.")
    } else {
        println("Lista de Álbumes:")
        albumes.forEachIndexed { index, album ->
            println("${index + 1}. $album")
        }
    }
}


// Función para actualizar un álbum existente
private fun actualizarAlbum(albumes: MutableList<Album>) {
    println("Ingrese el nombre del álbum que desea actualizar:")
    val nombreBuscar = readLine() ?: ""

    val indice = albumes.indexOfFirst { it.nombre == nombreBuscar }
    if (indice != -1) {
        val albumActualizado = crearAlbumManual(albumes[indice])
        albumes[indice] = albumActualizado
        println("Álbum actualizado con éxito: $albumActualizado")
    } else {
        println("Álbum no encontrado.")
    }
}

// Función para crear un álbum manualmente con opción de mantener valores actuales
private fun crearAlbumManual(albumExistente: Album): Album {
    println("Ingrese el nuevo nombre del álbum (o deje en blanco para mantener el actual: ${albumExistente.nombre}):")
    val nuevoNombre = readLine() ?: albumExistente.nombre

    val nuevaFechaLanzamiento = solicitarFecha("Ingrese la nueva fecha de lanzamiento del álbum (o deje en blanco para mantener la actual: ${albumExistente.fechaLanzamiento}):")

    println("Ingrese el nuevo número de canciones del álbum (o deje en blanco para mantener el actual: ${albumExistente.numeroCanciones}):")
    val nuevoNumeroCanciones = readLine()?.toIntOrNull() ?: albumExistente.numeroCanciones

    println("Ingrese el nuevo precio del álbum (o deje en blanco para mantener el actual: ${albumExistente.precio}):")
    val nuevoPrecio = readLine()?.toDoubleOrNull() ?: albumExistente.precio

    println("Ingrese el nuevo nombre del artista del álbum (o deje en blanco para mantener el actual: ${albumExistente.artistaNombre}):")
    val nuevoArtistaNombre = readLine() ?: albumExistente.artistaNombre

    return Album(nuevoNombre, nuevaFechaLanzamiento, nuevoNumeroCanciones, nuevoPrecio, nuevoArtistaNombre)
}

// Función para eliminar álbumes de la lista de álbumes.
// Se solicita al usuario el nombre del álbum a eliminar.
// Se busca el álbum en la lista y, si se encuentra, se elimina.
// Si no se encuentra, se muestra un mensaje indicando que el álbum no fue encontrado.
private fun eliminarAlbum(albumes: MutableList<Album>) {
    println("Ingrese el nombre del álbum que desea eliminar:")
    val nombreEliminar = readLine() ?: ""

    val indice = albumes.indexOfFirst { it.nombre == nombreEliminar }
    if (indice != -1) {
        val albumEliminado = albumes.removeAt(indice)
        println("Álbum eliminado con éxito: $albumEliminado")
    } else {
        println("Álbum no encontrado.")
    }
}
