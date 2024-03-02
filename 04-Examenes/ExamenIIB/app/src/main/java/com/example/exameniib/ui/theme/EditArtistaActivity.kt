package com.example.exameniib.ui.theme

import MainActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditArtistaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_artista)

        val idArtista = intent.getStringExtra("id")

        val btnActualizarArtista = findViewById<Button>(R.id.btn_actualizar_artista)
        btnActualizarArtista.setOnClickListener {
            actualizarArtista()
        }

        val btnBackEdit = findViewById<Button>(R.id.btn_back_edit_a)
        btnBackEdit.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
    }

    private fun actualizarArtista() {
        val id = findViewById<EditText>(R.id.et_id_artista)
        val nombre = findViewById<EditText>(R.id.et_nombre_artista)
        val fechaNacimiento = findViewById<EditText>(R.id.et_fecha_nacimiento_artista)
        val descripcion = findViewById<EditText>(R.id.et_descripcion_artista)
        val calificacion = findViewById<EditText>(R.id.et_calificacion_artista)

        val artista = hashMapOf(
            "id" to id.text.toString().toInt(),
            "nombre" to nombre.text.toString(),
            "fechaNacimiento" to fechaNacimiento.text.toString(),
            "descripcion" to descripcion.text.toString(),
            "calificacion" to calificacion.text.toString().toInt()
        )

        // Actualizar el documento del artista en Firestore
        db.collection("artistas")
            .document(id.text.toString())
            .set(artista)
            .addOnSuccessListener {
                mostrarSnackbar("Artista actualizado correctamente")
            }
            .addOnFailureListener { e ->
                mostrarSnackbar("Error al actualizar el artista: $e")
            }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_edit_artista),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
