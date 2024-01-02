import java.io.Serializable
import java.util.Date

data class Artista(
    val nombre: String,
    val fechaNacimiento: Date,
    val activo: Boolean,
    val numeroAlbumes: Int,
    val valorTotalVentas: Double
) : Serializable