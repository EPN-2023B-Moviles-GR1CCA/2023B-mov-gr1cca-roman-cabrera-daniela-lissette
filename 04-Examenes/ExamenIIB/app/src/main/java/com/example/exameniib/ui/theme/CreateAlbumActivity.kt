package com.example.exameniib.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.text.ParseException



class CreateAlbumActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var idArtista = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_album)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

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

    private fun crearAlbum() {
        val id = findViewById<EditText>(R.id.input_id_album)
        val nombre = findViewById<EditText>(R.id.input_nombre_album)
        val fechaLanzamiento = findViewById<EditText>(R.id.input_fecha_lanzamiento_album)
        val duracion = findViewById<EditText>(R.id.input_duracion_album)
        val esColaborativo = findViewById<CheckBox>(R.id.chk_colaborativo_album).isChecked

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fechaTexto = fechaLanzamiento.text.toString()
        val fecha: Date? = try {
            dateFormat.parse(fechaTexto)
        } catch (ex: ParseException) {
            ex.printStackTrace()
            null
        }

        if (fecha != null) {
            // Crear un mapa con los datos del álbum
            val album = hashMapOf(
                "id" to id.text.toString().toInt(),
                "nombre" to nombre.text.toString(),
                "fechaLanzamiento" to fecha,
                "duracion" to duracion.text.toString().toInt(),
                "idArtista" to idArtista,
                "esColaborativo" to esColaborativo
            )

            // Agregar el álbum a Firestore
            db.collection("albumes")
                .add(album)
                .addOnSuccessListener { documentReference ->
                    mostrarSnackbar("Se ha creado el álbum ${nombre.text} y el ID del artista es $idArtista")
                    val intent = Intent(this, VerAlbumesActivity::class.java)
                    intent.putExtra("id", idArtista)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    mostrarSnackbar("Error al crear el álbum: $e")
                }
        } else {
            mostrarSnackbar("Formato de fecha incorrecto")
        }
    }


    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.form_create_album),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
