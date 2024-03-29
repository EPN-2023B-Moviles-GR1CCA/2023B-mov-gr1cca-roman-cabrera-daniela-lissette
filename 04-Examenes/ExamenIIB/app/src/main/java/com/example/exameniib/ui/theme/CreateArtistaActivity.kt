package com.example.exameniib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date


class CreateArtistaActivity : AppCompatActivity() {

    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_artista)

        val btnCrearArtista = findViewById<Button>(R.id.btn_crear_artista)
        btnCrearArtista.setOnClickListener {
            crearArtista()
        }
    }

    private fun crearArtista() {
        val id = findViewById<EditText>(R.id.input_id)
        val nombre = findViewById<EditText>(R.id.input_nombre)
        val fechaNacimiento = findViewById<EditText>(R.id.input_fecha_nacimiento)
        val descripcion = findViewById<EditText>(R.id.input_descripcion)
        val calificacion = findViewById<EditText>(R.id.input_calificacion)

        // Creamos un objeto SimpleDateFormat para analizar la cadena de fecha
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        // Intentamos analizar la cadena de fecha y convertirla en un objeto Date
        var fechaNacimientoDate: Date? = null
        try {
            fechaNacimientoDate = dateFormat.parse(fechaNacimiento.text.toString())
        } catch (e: Exception) {
            // Manejar cualquier error de análisis aquí
            e.printStackTrace()
        }

        // Si el análisis de la fecha fue exitoso, continuamos con la creación del artista
        if (fechaNacimientoDate != null) {
            // Crear un mapa con los datos del artista
            val artista = hashMapOf(
                "id" to id.text.toString().toInt(),
                "nombre" to nombre.text.toString(),
                "fechaNacimiento" to fechaNacimientoDate,
                "descripcion" to descripcion.text.toString(),
                "calificacion" to calificacion.text.toString().toInt()
            )

            // Agregar el artista a Firestore
            db.collection("artistas")
                .add(artista)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Artista creado con ID: ${documentReference.id}")
                    mostrarSnackbar("Se ha creado el artista ${nombre.text}")
                    irActividad(MainActivity::class.java)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al crear el artista", e)
                }
        } else {
            // Manejar el caso en que la fecha no pudo ser analizada correctamente
            mostrarSnackbar("Fecha de nacimiento no válida")
        }
    }


    // Métodos auxiliares
    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_create_artista),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "CreateArtistaActivity"
    }
}
