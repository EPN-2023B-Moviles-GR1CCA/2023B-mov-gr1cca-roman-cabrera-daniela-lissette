package com.example.examenib

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.examenib.database.CrudArtista
import com.example.examenib.database.BDMemoria
import com.example.examenib.ui.theme.CreateArtistaActivity
import com.example.examenib.ui.theme.EditArtistaActivity
import com.example.examenib.ui.theme.VerAlbumesActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    val arregloArtistas = BDMemoria.arregloArtista
    var posicionItemSeleccionado = -1
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val selectedItem = arregloArtistas[posicionItemSeleccionado]
                val intent = Intent(this, EditArtistaActivity::class.java)
                intent.putExtra("id", selectedItem.id.toString())
                startActivity(intent)
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                return true
            }

            R.id.mi_ver_albumes -> {
                val selectedItem = arregloArtistas[posicionItemSeleccionado]
                val intent = Intent(this, VerAlbumesActivity::class.java)
                intent.putExtra("id", selectedItem.id.toString())
                startActivity(intent)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layout_main),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_list_artista)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloArtistas
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)

        val btnCrearArtista = findViewById<View>(R.id.btn_ir_crear_artista)
        btnCrearArtista.setOnClickListener {
            irActividad(CreateArtistaActivity::class.java)
        }
    }

    fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


    fun eliminarArtista(id: Int) {
        val listView = findViewById<ListView>(R.id.lv_list_artista)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloArtistas
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        var idArtistaAEliminar = arregloArtistas[id].id
        CrudArtista().eliminarAlbumesDelArtista(idArtistaAEliminar)
        arregloArtistas.removeAt(
            id
        )
    }

    fun abrirDialogo(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                eliminarArtista(id)
                mostrarSnackbar("Artista eliminado")
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
