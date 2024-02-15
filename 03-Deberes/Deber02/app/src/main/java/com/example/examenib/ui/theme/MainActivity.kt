package com.example.examenib

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.examenib.models.Artista
import com.example.examenib.repository.ArtistaRepository
import com.example.examenib.ui.theme.CreateArtistaActivity
import com.example.examenib.ui.theme.EditArtistaActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    var artistas = arrayListOf<Artista>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        BDD.bddAplicacion = ArtistaRepository(this)
        artistas = BDD.bddAplicacion!!.obtenerArtistas()

        if(artistas.size != 0){
            val listView = findViewById<ListView>(R.id.lv_list_artista)
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                artistas
            )

            listView.adapter = adaptador
            adaptador.notifyDataSetChanged()
            registerForContextMenu(listView)
        } else {
            mostrarSnackbar("No existen artistas")
        }

        val btnCrearArtista = findViewById<Button>(R.id.btn_ir_crear_artista)
        btnCrearArtista.setOnClickListener {
            val intent = Intent(this,  CreateArtistaActivity::class.java)
            callbackContenidoIntentExplicito.launch(intent)
        }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.constraint_artistas),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    var posicionItemSeleccionado = 0
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar ->{
                val idArtista = artistas[posicionItemSeleccionado].id
                val nombreArtista = artistas[posicionItemSeleccionado].nombre
                val extras = Bundle()
                extras.putInt("idArtista", idArtista)
                extras.putString("nombreArtista", nombreArtista)
                irEdicionArtista(EditArtistaActivity::class.java, extras)
                return true
            }
            R.id.mi_eliminar -> {
                mostrarSnackbar(artistas[posicionItemSeleccionado].nombre)
                val result: Boolean = abrirDialogo(artistas[posicionItemSeleccionado].id.toString())
                return result
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirDialogo(idArtista: String): Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar a este artista?")

        var eliminacionExitosa = false

        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                val respuesta = BDD.bddAplicacion?.eliminarArtistaPorId(idArtista)
                if (respuesta == true) {
                    mostrarSnackbar("Artista eliminado exitosamente")
                    cargarListaArtistas()
                    eliminacionExitosa = true
                } else {
                    mostrarSnackbar("No se pudo eliminar al artista")
                    eliminacionExitosa = false
                }
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()

        return eliminacionExitosa
    }

    fun irEdicionArtista(clase: Class<*>, datosExtras: Bundle? = null) {
        val intent = Intent(this, clase)
        if (datosExtras != null) {
            intent.putExtras(datosExtras)
        }
        callbackContenidoIntentExplicito.launch(intent)
    }

    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    val data = result.data
                    cargarListaArtistas()
                    mostrarSnackbar("${data?.getStringExtra("message")}")
                }
            }
        }

    private fun cargarListaArtistas() {
        artistas = BDD.bddAplicacion!!.obtenerArtistas()
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            artistas
        )
        val listView = findViewById<ListView>(R.id.lv_list_artista)
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
    }
}
