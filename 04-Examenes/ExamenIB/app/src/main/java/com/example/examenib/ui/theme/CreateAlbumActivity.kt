package com.example.examenib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.examenib.R
import com.example.examenib.database.CrudAlbum
import com.google.android.material.snackbar.Snackbar
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat

class CreateAlbumActivity : AppCompatActivity() {

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
        val idartista= parseInt(idArtista)
        val fechaLanzamiento = findViewById<EditText>(R.id.input_fecha_lanzamiento_album)
        val duracion = findViewById<EditText>(R.id.input_duracion_album)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album).isChecked

        CrudAlbum().crearAlbum(
            id.text.toString().toInt(),
            nombre.text.toString(),
            SimpleDateFormat("dd/MM/yyyy").parse(fechaLanzamiento.text.toString()),
            duracion.text.toString().toInt(),
            idartista,
            esColaborativo,
        )

        mostrarSnackbar("Se ha creado el álbum ${nombre.text} y el ID del artista es $idartista")

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
