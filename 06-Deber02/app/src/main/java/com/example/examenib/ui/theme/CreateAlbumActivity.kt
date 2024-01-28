package com.example.examenib.ui.theme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.examenib.R
import com.google.android.material.snackbar.Snackbar

class AlbumCreacionActivity : AppCompatActivity() {

    var idArtista = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        idArtista = intent.getStringExtra("idArtista").toString()
        mostrarSnackbar("ID del artista: $idArtista")

        val btnCrearAlbum = findViewById<Button>(R.id.btn_crear_album)
        btnCrearAlbum.setOnClickListener {
            crearAlbum()
        }

        val btnBack = findViewById<Button>(R.id.btn_back_create_album)
        btnBack.setOnClickListener {
            val intent = Intent(this, VerAlbumesActivity::class.java)
            intent.putExtra("id", idArtista)
            startActivity(intent)
        }
    }

    fun crearAlbum() {
        val id = findViewById<EditText>(R.id.input_id_album)
        val nombre = findViewById<EditText>(R.id.input_nombre_album)
        val fechaLanzamiento = findViewById<EditText>(R.id.input_fecha_lanzamiento_album)
        val duracion = findViewById<EditText>(R.id.input_duracion_album)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album).isChecked

        // Validaciones de los campos, puedes agregar más según tus necesidades
        if (id.text.isBlank() || nombre.text.isBlank() || fechaLanzamiento.text.isBlank() || duracion.text.isBlank()) {
            mostrarSnackbar("Por favor, complete todos los campos.")
            return
        }

        mostrarSnackbar("Se ha creado el álbum ${nombre.text} y el ID del artista es $idArtista")

        val intent = Intent(this, VerAlbumesActivity::class.java)
        intent.putExtra("id", idArtista)
        startActivity(intent)
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_create_album),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
