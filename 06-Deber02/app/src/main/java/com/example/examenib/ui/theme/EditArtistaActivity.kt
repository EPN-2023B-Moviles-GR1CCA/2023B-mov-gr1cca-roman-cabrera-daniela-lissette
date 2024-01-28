package com.example.examenib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.examenib.BDD
import com.example.examenib.MainActivity
import com.example.examenib.R
import com.example.examenib.models.Artista
import com.example.examenib.repository.ArtistaRepository
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class EditarArtistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_artista)

        val idArtista = intent.getStringExtra("id")

        val artista = BDD.bddAplicacion?.obtenerArtistaPorId(idArtista)

        if (artista != null) {
            llenarInputs(artista)
            mostrarSnackbar("$artista")

            val btnActualizar = findViewById<Button>(R.id.btn_actualizar_artista)
            btnActualizar.setOnClickListener {
                actualizarArtista(artista)
                mostrarSnackbar("Artista actualizado")
                irActividad(MainActivity::class.java)
            }

            val btnBack = findViewById<Button>(R.id.btn_back_edit_a)
            btnBack.setOnClickListener {
                irActividad(MainActivity::class.java)
            }
        } else {
            mostrarSnackbar("No se encontró el artista para editar")
            finish()
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.form_edit_artista),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    fun llenarInputs(artista: Artista) {
        val id = findViewById<EditText>(R.id.et_id_artista)
        val nombre = findViewById<EditText>(R.id.et_nombre_artista)
        val fechaNacimiento = findViewById<EditText>(R.id.et_fecha_nacimiento_artista)
        val descripcion = findViewById<EditText>(R.id.et_descripcion_artista)
        val calificacion = findViewById<EditText>(R.id.et_calificacion_artista)

        id.setText(artista.id.toString())
        nombre.setText(artista.nombre)
        fechaNacimiento.setText(SimpleDateFormat("dd/MM/yyyy").format(artista.fechaNacimiento))
        descripcion.setText(artista.descripcion)
        calificacion.setText(artista.calificacion.toString())
    }

    fun actualizarArtista(artista: Artista) {
        val id = findViewById<EditText>(R.id.et_id_artista)
        val nombre = findViewById<EditText>(R.id.et_nombre_artista)
        val fechaNacimiento = findViewById<EditText>(R.id.et_fecha_nacimiento_artista)
        val descripcion = findViewById<EditText>(R.id.et_descripcion_artista)
        val calificacion = findViewById<EditText>(R.id.et_calificacion_artista)

        val respuesta = BDD.bddAplicacion?.editarArtista(
            id.text.toString().toInt(),
            nombre.text.toString(),
            SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimiento.text.toString()),
            descripcion.text.toString(),
            calificacion.text.toString().toDouble()
        )

        if (respuesta == true) {
            // Éxito al actualizar el artista
        } else {
            mostrarSnackbar("No se pudo actualizar el artista")
        }
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
