package com.example.examenib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.examenib.BDD
import com.example.examenib.R
import com.example.examenib.models.Album
import com.example.examenib.repository.AlbumRepository
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class EditAlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_album)

        val idAlbum = intent.getIntExtra("idAlbum", 0)
        val album = BDD.bddAplicacion?.obtenerAlbumPorId(idAlbum)

        if (album != null) {
            llenarInputs(album)

            val btnActualizar = findViewById<Button>(R.id.btn_actualizar_album)
            btnActualizar.setOnClickListener {
                actualizarAlbum(album)
                mostrarSnackbar("Álbum actualizado")

                val intent = Intent(this, VerAlbumesActivity::class.java)
                intent.putExtra("id", album.artista.id.toString())
                startActivity(intent)
            }

            val btnBack = findViewById<Button>(R.id.btn_back_edit_a)
            btnBack.setOnClickListener {
                val intent = Intent(this, VerAlbumesActivity::class.java)
                intent.putExtra("id", album.artista.id.toString())
                startActivity(intent)
            }
        } else {
            mostrarSnackbar("No se encontró el álbum para editar")
            finish()
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_edit_album),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun llenarInputs(album: Album) {
        val id = findViewById<EditText>(R.id.et_id_a)
        val nombre = findViewById<EditText>(R.id.et_nombre_a)
        val duracion = findViewById<EditText>(R.id.et_duracion_a)
        val fechaLanzamiento = findViewById<EditText>(R.id.et_fecha_a)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album)

        id.setText(album.id.toString())
        nombre.setText(album.nombre)
        duracion.setText(album.duracion.toString())
        fechaLanzamiento.setText(SimpleDateFormat("dd/MM/yyyy").format(album.fechaLanzamiento))
        esColaborativo.isChecked = album.esColaborativo
    }

    fun actualizarAlbum(album: Album) {
        val id = findViewById<EditText>(R.id.et_id_a)
        val nombre = findViewById<EditText>(R.id.et_nombre_a)
        val duracion = findViewById<EditText>(R.id.et_duracion_a)
        val fechaLanzamiento = findViewById<EditText>(R.id.et_fecha_a)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album)
        val idartista = album.artista.id

        val respuesta = BDD.bddAplicacion?.editarAlbum(
            id.text.toString().toInt(),
            nombre.text.toString(),
            SimpleDateFormat("dd/MM/yyyy").parse(fechaLanzamiento.text.toString()),
            duracion.text.toString().toInt(),
            idartista,
            esColaborativo.isChecked
        )

        if (respuesta == true) {
            // Éxito al actualizar el álbum
        } else {
            mostrarSnackbar("No se pudo actualizar el álbum")
        }
    }
}
