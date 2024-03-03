package com.example.exameniib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import java.text.ParseException
import java.util.Date
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.example.exameniib.models.Album
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat

class EditAlbumActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_album)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        val idAlbum = intent.getIntExtra("idAlbum", 0)
        val albumRef = db.collection("albumes").document(idAlbum.toString())

        albumRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val album = document.toObject(Album::class.java)
                    if (album != null) {
                        llenarInputs(album)
                    }
                } else {
                    mostrarSnackbar("No se encontró el álbum")
                }
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener el álbum: $exception")
            }

        val btnActualizar = findViewById<Button>(R.id.btn_actualizar_album)
        btnActualizar.setOnClickListener {
            actualizarAlbum(albumRef)
        }

        val btnBack = findViewById<Button>(R.id.btn_back_edit_a)
        btnBack.setOnClickListener {
            irActividad(VerAlbumesActivity::class.java)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_edit_album),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun llenarInputs(album: Album) {
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

    private fun actualizarAlbum(albumRef: DocumentReference) {
        val id = findViewById<EditText>(R.id.et_id_a)
        val nombre = findViewById<EditText>(R.id.et_nombre_a)
        val duracion = findViewById<EditText>(R.id.et_duracion_a)
        val fechaLanzamiento = findViewById<EditText>(R.id.et_fecha_a)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fechaTexto = fechaLanzamiento.text.toString()
        val fecha: Date? = try {
            dateFormat.parse(fechaTexto)
        } catch (ex: ParseException) {
            ex.printStackTrace()
            null
        }

        if (fecha != null) {
            // Crear un HashMap para los datos actualizados del álbum
            val album = hashMapOf<String, Any>(
                "id" to id.text.toString().toInt(),
                "nombre" to nombre.text.toString(),
                "duracion" to duracion.text.toString().toInt(),
                "fechaLanzamiento" to fecha,
                "esColaborativo" to esColaborativo.isChecked
            )

            albumRef.update(album)
                .addOnSuccessListener {
                    mostrarSnackbar("Álbum actualizado")
                    irActividad(VerAlbumesActivity::class.java)
                }
                .addOnFailureListener { e ->
                    mostrarSnackbar("Error al actualizar el álbum: $e")
                }
        } else {
            mostrarSnackbar("Formato de fecha incorrecto")
        }
    }



    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
