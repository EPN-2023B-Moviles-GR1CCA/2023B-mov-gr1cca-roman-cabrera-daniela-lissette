package com.example.examenib.ui.theme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examenib.MainActivity
import com.example.examenib.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class CreateArtistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_artista)

        val btnCrearArtista = findViewById<Button>(R.id.btn_crear_artista)

        btnCrearArtista.setOnClickListener {
            crearArtista()
            irActividad(MainActivity::class.java)
        }

        val btnBackC = findViewById<Button>(R.id.btn_back_create_a)
        btnBackC.setOnClickListener {
            irActividad(MainActivity::class.java)
        }
    }

    fun crearArtista() {
        val id = findViewById<EditText>(R.id.input_id)
        val nombre = findViewById<EditText>(R.id.input_nombre)
        val fechaNacimiento = findViewById<EditText>(R.id.input_fecha_nacimiento)
        val descripcion = findViewById<EditText>(R.id.input_descripcion)
        val calificacion = findViewById<EditText>(R.id.input_calificacion)

        mostrarSnackbar("Se ha creado el artista ${nombre.text}")
        irActividad(MainActivity::class.java)
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_create_artista),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
