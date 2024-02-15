import java.io.Serializable
import java.util.Date

data class Album(
    val nombre: String,
    val fechaLanzamiento: Date,
    val numeroCanciones: Int,
    val precio: Double,
    val artistaNombre: String
) : Serializable