package com.example.exameniib.ui.theme

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.example.exameniib.models.Album
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class VerAlbumesActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ArrayAdapter<Album>
    private lateinit var listView: ListView
    private val arregloAlbumesPorArtista = ArrayList<Album>()

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_album, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val intent = Intent(this, EditAlbumActivity::class.java)
                val album = arregloAlbumesPorArtista[posicionItemSeleccionado]
                intent.putExtra("idAlbum", album.id)
                startActivity(intent)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_albumes)
        val idArtista = intent.getStringExtra("id")
        mostrarSnackbar("Ver álbumes de: $idArtista")

        listView = findViewById(R.id.lv_list_albumes)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloAlbumesPorArtista)
        listView.adapter = adapter
        registerForContextMenu(listView)

        val btnCrearAlbum = findViewById<Button>(R.id.btn_ir_crear_album)
        btnCrearAlbum.setOnClickListener {
            val intentCreate = Intent(this, CreateAlbumActivity::class.java)
            intentCreate.putExtra("idArtista", idArtista)
            startActivity(intentCreate)
        }

        val btnBack = findViewById<Button>(R.id.btn_back_ver_a)
        btnBack.setOnClickListener {
            irActividad(MainActivity::class.java)
        }

        obtenerAlbumesPorArtista(idArtista)
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            texto,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    private fun obtenerAlbumesPorArtista(idArtista: String?) {
        db.collection("albumes")
            .whereEqualTo("idArtista", idArtista)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val album = document.toObject(Album::class.java)
                    arregloAlbumesPorArtista.add(album)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener álbumes: $exception")
            }
    }

    private fun eliminarAlbum(posicion: Int) {
        val idAlbumAEliminar = arregloAlbumesPorArtista[posicion].id.toString()
        arregloAlbumesPorArtista.removeAt(posicion)
        adapter.notifyDataSetChanged()
        db.collection("albumes").document(idAlbumAEliminar)
            .delete()
            .addOnSuccessListener {
                mostrarSnackbar("Álbum eliminado correctamente")
            }
            .addOnFailureListener { e ->
                mostrarSnackbar("Error al eliminar el álbum: $e")
            }
    }

    private fun abrirDialogo(posicion: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar"
        ) { dialog, _ ->
            eliminarAlbum(posicion)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }
}
