package com.example.examenib.ui.theme

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.examenib.MainActivity
import com.example.examenib.R
import com.example.examenib.database.BDMemoria
import com.example.examenib.models.Album
import com.google.android.material.snackbar.Snackbar
import java.lang.Integer.parseInt

class VerAlbumesActivity : AppCompatActivity() {

    val arregloAlbumes = BDMemoria.arregloAlbum
    var posicionItemSeleccionado = -1
    var arregloAlbumesPorArtista = arrayListOf<Album>()

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_album, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val intent = Intent(this, EditAlbumActivity::class.java)
                intent.putExtra("idAlbum", arregloAlbumesPorArtista[posicionItemSeleccionado].id)
                startActivity(intent)
                return true
            }
            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_albumes)
        val idArtista = intent.getStringExtra("id")
        mostrarSnackbar("Ver álbumes de: $idArtista")

        arregloAlbumesPorArtista =
            arregloAlbumes.filter { album -> album.artista.id == parseInt(idArtista) } as ArrayList<Album>

        val listView = findViewById<ListView>(R.id.lv_list_albumes)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloAlbumesPorArtista
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
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
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layout_intents),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


    fun eliminarAlbum(id: Int) {

        val listView = findViewById<ListView>(R.id.lv_list_albumes)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloAlbumesPorArtista
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        val idAlbumAEliminar = arregloAlbumesPorArtista[id].id
        arregloAlbumesPorArtista.removeAt(id)

        BDMemoria.arregloAlbum.removeIf { album -> album.id == idAlbumAEliminar }
    }

    fun abrirDialogo(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                eliminarAlbum(id)
                mostrarSnackbar("Álbum eliminado")
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val dialogo = builder.create()
        dialogo.show()
    }

}
